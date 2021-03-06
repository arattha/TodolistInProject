package com.web.tip.member;

import com.web.tip.common.MemberHasTeam;
import com.web.tip.common.MemberHasTeamDao;
import com.web.tip.config.security.TokenProvider;
import com.web.tip.error.CustomException;
import com.web.tip.error.ErrorCode;
import com.web.tip.jwt.TokenDto;
import com.web.tip.member.request.SignUpRequest;
import com.web.tip.member.request.UpdatePasswordRequest;
import com.web.tip.member.response.MemberResponse;
import com.web.tip.member.response.TeamMemberResponse;
import com.web.tip.member.security.Authority;
import com.web.tip.mypage.MemberDetail;
import com.web.tip.mypage.MemberDetailDto;
import com.web.tip.mypage.MemberDetailService;
import com.web.tip.util.IdGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    private MemberDetailService memberDetailService;
    private IdGenerator idGenerator;
    private MemberHasTeamDao memberHasTeamDao;

    @Transactional
    public boolean nicknameCheck(String nickname) {
        return memberDao.existsByNickname(nickname);
    }

    @Transactional
    public boolean signUp(SignUpRequest signUpRequest) {

        memberDao.findMemberByNickname(signUpRequest.getNickname())
                .ifPresent(member -> new CustomException(ErrorCode.MEMBER_DUPLICATE_RESOURCE));

        String mid = idGenerator.generateId();
        while (memberDao.existsById(mid)) {
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
        memberDetailService.createMemberDetail(member, signUpRequest);

        return true;
    }

    @Transactional
    public Optional<TokenDto> login(String password, String nickname) {
        // Login ID / PW ???????????? AuthenticationToken ??????, ????????? mid??? id???
        // mid + nickname??? password??? ??????????????? ??????
        // ??????, password??? ???????????? uid??? passwordEncoder??? ??????????????? ?????? ??????????????????.
        String mid = memberDao.findMemberByNickname(nickname)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND))
                .getId();

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(mid, password);

        // ?????? ????????? ???????????? JWT ?????? ??????
        TokenDto tokenDto;
        String key = "";

        // ?????? ????????? ???????????????
        // autheticate ???????????? ????????? ???, CustomUserDetailsService?????? ???????????? loadUserByUsername ???????????? ??????
        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            tokenDto = tokenProvider.generateTokenDto(authentication);
            key = authentication.getName();

            redisTemplate.opsForHash().put(key, "rt", tokenDto.getRefreshToken());
            redisTemplate.opsForHash().put(key, "at", tokenDto.getAccessToken());

            long diffTime = tokenDto.getRefreshTokenExpiresIn() - (new Date()).getTime();
            redisTemplate.expire(key, diffTime, TimeUnit.MILLISECONDS);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return Optional.empty();
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return Optional.ofNullable(tokenDto);
    }

    /**
     * Aceess Token??? Refresh Token??? ?????? ????????? ??????
     * Access Token??? ??????????????? ?????? ????????? ?????? ?????? ??????(nickname)??? ???????????? ???????????? ??????
     * Refresh Token??? ????????????????????? ???????????? ????????? ????????? ????????? ??????????????? ????????? ????????? ???????????? DB??? ????????????
     * Access Token??? ??????
     * <p>
     * at = access token
     * rt = refresh token
     *
     * @param at
     * @return
     */
    @Transactional
    public Optional<TokenDto> reissuance(String mid, String at) {
        // Access Token?????? Member ID ????????????
        Authentication authentication = tokenProvider.getAuthentication(at);
        String key = mid;
        TokenDto tokenDto = null;
        try {
            boolean hasKey = redisTemplate.hasKey(key);
            if (!hasKey || redisTemplate.opsForValue().get(at) != null) {
                return Optional.empty();
            }

            // ??????????????? Member mid??? ???????????? Refresh token ?????? ????????????.
            String getRefreshToken = (String) redisTemplate.opsForHash().get(key, "rt");
            String getAccessToken = (String) redisTemplate.opsForHash().get(key, "at");

            // Token ??????
            if (!tokenProvider.validateToken(getRefreshToken) || !at.equals(getAccessToken)) {
                return Optional.empty();
            }

            // ????????? ?????? ??????
            tokenDto = tokenProvider.generateTokenDto(authentication);

            // ????????? ?????? ????????????
            redisTemplate.opsForHash().put(key, "rt", tokenDto.getRefreshToken());
            redisTemplate.opsForHash().put(key, "at", tokenDto.getAccessToken());
            long diffTime = tokenDto.getRefreshTokenExpiresIn() - (new Date()).getTime();

            redisTemplate.expire(key, diffTime, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        // ?????? ??????
        return Optional.ofNullable(tokenDto);
    }

    /**
     * ??????????????? ????????????
     * ??????????????? ?????? ???????????? false??? ??????
     * ??????????????? ??????????????? redis??? access token??? ??? ?????? set??? ??????
     * ??????????????? ???????????? ?????????
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
            // ????????? ????????? ?????? ??????????????? ????????????
            boolean hasKey = redisTemplate.hasKey(key);
            if (!tokenProvider.validateToken(at) || !hasKey
                    || redisTemplate.opsForValue().get(at) != null) {
                return false;
            }

            redisTemplate.delete(mid);
            redisTemplate.opsForValue().set(at, "logout");
            // ????????? ????????? ?????? ???????????? logout blacklist??? ???????????? ??????????????? ??? ???????????? ????????????.
            long diffTime = tokenProvider.getExpireDate(at).getTime() - (new Date()).getTime();

            redisTemplate.expire(at, diffTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return true;
    }

    @Transactional
    public void changePassword(UpdatePasswordRequest request) {
        Member member = memberDao.findMemberById(request.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Member changedMember = Member.builder()
                .id(member.getId())
                .memberDetail(member.getMemberDetail())
                .authority(member.getAuthority())
                .nickname(member.getNickname())
                .isUse(member.isUse())
                .name(member.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        memberDao.save(changedMember);
    }

    @Transactional
    public Object getAllMembers() {

        List<Member> memberList = memberDao.findMemberByIsUse(true);
        if (memberList.isEmpty()) {
            return Collections.emptyList();
        }
        List<MemberResponse> result = new ArrayList<>();
        memberList.forEach(v -> result.add(new MemberResponse(v.getId(), v.getName(), MemberDetailDto.entityToDto(v.getMemberDetail()))));

        return result;
    }

    @Transactional(readOnly = true)
    public List<TeamMemberResponse> getMembersByTeam(String teamId){
        List<MemberHasTeam> memberHasTeams = memberHasTeamDao.findByTeamId(teamId);
        if(memberHasTeams.isEmpty())
            return Collections.emptyList();

        List<TeamMemberResponse> memberResponses = new ArrayList<>();
        for(MemberHasTeam memberHasTeam: memberHasTeams){
            if(!memberHasTeam.isUse())
                continue;
            Member member = memberDao.findMemberById(memberHasTeam.getMemberId())
                    .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
            memberResponses.add(TeamMemberResponse.builder()
                    .id(member.getId())
                    .name(member.getName())
                    .email(member.getMemberDetail().getEmail())
                    .build()
            );
        }
        return memberResponses;
    }

    @Transactional
    public boolean existsUserCheck(String nickname) {
        return memberDao.existsByNickname(nickname);
    }

    @Transactional
    public Member getMemberByNickName(String nickName) {
        return memberDao.findMemberByNickname(nickName)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public Member getMemberById(String id) {
        return memberDao.findMemberById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }


}
