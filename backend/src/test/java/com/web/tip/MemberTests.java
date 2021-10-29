package com.web.tip;

import com.web.tip.member.Member;
import com.web.tip.member.MemberService;
import com.web.tip.member.request.SignUpRequest;
import com.web.tip.util.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class MemberTests {

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private MemberService memberService;

    @Test
    void testIdGenerate(){
        for(int i = 0; i < 10; i++) {
            String id = idGenerator.generateId();
            Assertions.assertTrue(id.length() == 13, id);
        }
    }

    @Test
    void testSignup(){
        Assertions.assertTrue(memberService.signUp(SignUpRequest.builder()
                .nickname("test")
                .name("최준오")
                .password("1234")
                .build()));

        Member member = memberService.getMemberByNickName("choi");
        log.info(member.toString());
        Assertions.assertTrue(memberService.existsUserCheck("choi"));
    }

    @Test
    void testLogin(){
        Assertions.assertTrue(memberService.login("1234", "choi").isPresent());
    }

}
