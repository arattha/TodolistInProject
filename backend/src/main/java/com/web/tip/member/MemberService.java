package com.web.tip.member;

import com.web.tip.config.security.TokenProvider;
import com.web.tip.error.CustomException;
import com.web.tip.error.ErrorCode;
import com.web.tip.jwt.TokenDto;
import com.web.tip.member.request.SignUpRequest;
import com.web.tip.member.security.Authority;
import com.web.tip.util.IdGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService {
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    private PasswordEncoder passwordEncoder;
    private TokenProvider tokenProvider;
    private MemberDao memberDao;
    private RedisTemplate<String, Object> redisTemplate;

    private IdGenerator idGenerator;

    @Transactional
    public boolean nicknameCheck(String nickname) {
        return memberDao.existsByNickname(nickname);
    }

    @Transactional
    public boolean signUp(SignUpRequest signUpRequest) {

        memberDao.findMemberByNickname(signUpRequest.getNickname())
                .ifPresent(member -> new CustomException(ErrorCode.MEMBER_DUPLICATE_RESOURCE));

        String mid = idGenerator.generateId();
        while(memberDao.existsById(mid)){
            mid = idGenerator.generateId();
        }

        Member member = Member.builder()
                .id(mid)
                .name(signUpRequest.getName())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .nickname(signUpRequest.getNickname())
                .authority(Authority.ROLE_USER)
                .isUse(true)
                .build();

        memberDao.save(member);

        return true;
    }

    @Transactional
    public Optional<TokenDto> login(String password, String nickname) {
        // Login ID / PW 기반으로 AuthenticationToken 생성, 여기선 mid를 id로
        // mid + nickname을 password로 사용했음을 주의
        // 또한, password로 들어오는 uid는 passwordEncoder로 처리가안된 값이 들어와야한다.
        String mid = memberDao.findMemberByNickname(nickname)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND))
                .getId();

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(mid, password);

        // 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto;
        String key = "";

        // 실제 검증이 이루어진다
        // autheticate 메서드가 실행될 때, CustomUserDetailsService에서 만들었던 loadUserByUsername 메서드가 실행
        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            tokenDto = tokenProvider.generateTokenDto(authentication);
            key = authentication.getName();

            redisTemplate.opsForHash().put(key, "rt", tokenDto.getRefreshToken());
            redisTemplate.opsForHash().put(key, "at", tokenDto.getAccessToken());

            long diffTime = tokenDto.getRefreshTokenExpiresIn() - (new Date()).getTime();
            redisTemplate.expire(key, diffTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return Optional.ofNullable(tokenDto);
    }

    /**
     * Aceess Token과 Refresh Token을 받아 검증을 하고
     * Access Token이 만료됬다면 이를 복호화 하여 유저 정보(nickname)을 가져오고 저장소에 있는
     * Refresh Token이 만료되었는지를 검사하여 만료가 안됬고 정보가 일치한다면 새로운 토큰을 생성하고 DB에 저장하고
     * Access Token을 반환
     * <p>
     * at = access token
     * rt = refresh token
     *
     * @param at
     * @return
     */
    @Transactional
    public Optional<TokenDto> reissuance(String mid, String at) {
        // Access Token에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(at);

        String key = mid;
        TokenDto tokenDto = null;
        try {
            boolean hasKey = redisTemplate.hasKey(key);
            if (!hasKey || redisTemplate.opsForValue().get(at) != null) {
                return Optional.empty();
            }

            // 저장소에서 Member mid를 기반으로 Refresh token 값을 가져온다.
            String getRefreshToken = (String) redisTemplate.opsForHash().get(key, "rt");
            String getAccessToken = (String) redisTemplate.opsForHash().get(key, "at");

            // Token 검증
            if (!tokenProvider.validateToken(getRefreshToken) || !tokenProvider.validateToken(getAccessToken)
                    || !getAccessToken.equals(at)) {
                return Optional.empty();
            }

            // 새로운 토큰 생성
            tokenDto = tokenProvider.generateTokenDto(authentication);

            // 저장소 정보 업데이트
            redisTemplate.opsForHash().put(key, "rt", tokenDto.getRefreshToken());
            redisTemplate.opsForHash().put(key, "at", tokenDto.getAccessToken());
            long diffTime = tokenDto.getRefreshTokenExpiresIn() - (new Date()).getTime();

            redisTemplate.expire(key, diffTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        // 토큰 발급
        return Optional.ofNullable(tokenDto);
    }

    /**
     * 로그아웃을 처리한다
     * 로그아웃이 이미 되었다면 false를 반환
     * 로그아웃이 가능하다면 redis에 access token이 키 값인 set을 넣어
     * 로그아웃한 토큰임을 남긴다
     * <p>
     * at = access token
     * rt = refresh token
     *
     * @param at
     * @return
     */
    @Transactional
    public boolean logout(String mid, String at) {
        String key = mid;

        try {
            // 권한이 없거나 이미 로그아웃된 유저라면
            boolean hasKey = redisTemplate.hasKey(key);
            if (!tokenProvider.validateToken(at) || !hasKey
                    || redisTemplate.opsForValue().get(at) != null) {
                return false;
            }

            redisTemplate.delete(mid);
            redisTemplate.opsForValue().set(at, "logout");
            // 액세스 토큰의 남은 시간만큼 logout blacklist에 추가하여 로그아웃이 된 토큰임을 저장한다.
            long diffTime = tokenProvider.getExpireDate(at).getTime() - (new Date()).getTime();

            redisTemplate.expire(at, diffTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return true;
    }

    @Transactional
    public boolean existsUserCheck(String nickName) {
        return memberDao.existsByNickname(nickName);
    }

    @Transactional
    public Member getMemberByNickName(String nickName) {
        return memberDao.findMemberByNickname(nickName)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public Optional<Member> getMember(String nickname){

        try{
            return memberDao.findMemberByNickname(nickname);
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public List<Member> getAllMember(){

        try{
            return memberDao.findAll();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
