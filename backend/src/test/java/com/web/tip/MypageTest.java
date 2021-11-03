package com.web.tip;

import com.web.tip.member.Member;
import com.web.tip.member.MemberService;
import com.web.tip.member.request.UpdateMemberRequest;
import com.web.tip.mypage.MemberDetailDto;
import com.web.tip.mypage.MemberDetailService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class MypageTest {

    @Autowired
    private MemberDetailService memberDetailService;

    @Autowired
    private MemberService memberService;

    @Test
    void testUpdateMyPage() {
        Member member = memberService.getMemberByNickName("test");
        log.info("nickName: test");

        UpdateMemberRequest updateMemberRequest = UpdateMemberRequest.builder()
                .id(member.getId())
                .phone("1234")
                .email("eqwr")
                .build();

        MemberDetailDto response = memberDetailService.updateMemberDetail(updateMemberRequest);
        log.info("response: " + response.toString());

        Assertions.assertEquals("eqwr", response.getEmail(), "이메일 업데이트 오류");
        Assertions.assertEquals("1234", response.getPhone(), "전화번호 업데이트 오류");
    }

    @Test
    void testFindMemberDetail() {
        Member member = memberService.getMemberByNickName("test");
        MemberDetailDto memberDetailDto = memberDetailService.findMemberDetail(member);

        log.info(memberDetailDto.toString());

        Assertions.assertEquals("eqwr", memberDetailDto.getEmail());
        Assertions.assertEquals("1234", memberDetailDto.getPhone());
    }

}
