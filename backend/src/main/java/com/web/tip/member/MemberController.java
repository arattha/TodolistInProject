package com.web.tip.member;

import com.web.tip.BasicResponse;
import com.web.tip.jwt.TokenDto;
import com.web.tip.member.request.LoginRequest;
import com.web.tip.member.request.SignUpRequest;
import com.web.tip.member.request.UpdatePasswordRequest;
import com.web.tip.member.response.LoginResponse;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@CrossOrigin(origins = {"*"}, maxAge = 6000)
@RestController
@AllArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private MemberService memberService;

    private static final String ACCESS_TOKEN = "accessToken";
    private static final String SUCCESS = "success";

    @PostMapping("/signup")
    public ResponseEntity<BasicResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
        log.info("sign up request");
        boolean signUpResult = memberService.signUp(signUpRequest);

        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "Success";
        result.object = signUpResult;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/update_password")
    @ApiOperation(value = "패스워드 변경")
    public ResponseEntity<Object> updatePassword(@RequestBody UpdatePasswordRequest request) {
        log.info("member:{} 의 패스워드 변경");
        memberService.changePassword(request);

        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "Success";

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/login")
    @ApiOperation(value = "일반 로그인")
    public Object login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        BasicResponse result = new BasicResponse();
        LoginResponse loginResponse;

        result.status = true;
        result.data = SUCCESS;
        // 만약 존재하지 않는 유저라면 회원가입을 유도하기 위해 isMember를 false로 반환

        System.out.println(memberService.existsUserCheck(loginRequest.getNickname()));
        if (!memberService.existsUserCheck(loginRequest.getNickname())) {
            loginResponse = LoginResponse.builder()
                    .isMember(false)
                    .build();

            result.object = loginResponse;
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

        // 로그인을 처리하기 위한 토큰을 발급받고 쿠키에 담는다.
        Optional<TokenDto> tokenDtoOptional = memberService.login(loginRequest.getPassword(), loginRequest.getNickname());
        if (tokenDtoOptional.isPresent()) {
            // 현재 mid의 Member 정보를 가져와 반환 객체인 loginResponse에 필요한 내용을 담는다.
            Member member = memberService.getMemberByNickName(loginRequest.getNickname());

            loginResponse = LoginResponse.builder()
                    .mid(member.getId())
                    .nickname(member.getNickname())
                    .isMember(true)
                    .build();

            result.object = loginResponse;

            Cookie cookie = getAuthCookie(tokenDtoOptional);
            response.addCookie(cookie);
        } else {
            result.object = "fail";
            result.status = false;
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/ncheck")
    public Object nicknameDupCheck(String nickname) {
        BasicResponse result = new BasicResponse();

        result.status = true;
        result.data = SUCCESS;
        result.object = memberService.nicknameCheck(nickname);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public Object logout(@RequestBody String mid, HttpServletRequest request, HttpServletResponse response) {
        log.info("logout >>> " + mid);
        BasicResponse result = new BasicResponse();

        String accessToken = getAccessTokenToCookie(request);
        Cookie cookie = new Cookie(ACCESS_TOKEN, "");

        cookie.setMaxAge(0);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);

        result.data = SUCCESS;
        result.status = true;
        result.object = memberService.logout(mid, accessToken);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/reissu")
    @ApiOperation(value = "토큰 재발급")
    public Object reissuance(@RequestBody String mid, HttpServletRequest request, HttpServletResponse response) {
        BasicResponse result = new BasicResponse();

        result.status = true;
        result.data = SUCCESS;

        String accessToken = getAccessTokenToCookie(request);

        if (accessToken == null || "".equals(accessToken) || accessToken.length() <= 0) {
            result.object = false;
        } else {
            result.object = true;

            Optional<TokenDto> getToken = memberService.reissuance(mid, accessToken);

            Cookie cookie = getAuthCookie(getToken);
            response.addCookie(cookie);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private String getAccessTokenToCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (ACCESS_TOKEN.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return "";
    }

    private Cookie getAuthCookie(Optional<TokenDto> optionalTokenDto) {
        Cookie cookie = null;

        if (optionalTokenDto.isPresent()) {
            TokenDto tokenDto = optionalTokenDto.get();

            cookie = new Cookie(ACCESS_TOKEN, tokenDto.getAccessToken());
            // 일주일로 설정
            cookie.setMaxAge(60 * 60 * 24 * 7);
        } else {
            cookie = new Cookie(ACCESS_TOKEN, "logout");
            // 일주일로 설정
            cookie.setMaxAge(0);
        }

        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        return cookie;
    }
}
