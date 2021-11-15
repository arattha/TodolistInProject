package com.web.tip.mypage;

import com.web.tip.BasicResponse;
import com.web.tip.member.Member;
import com.web.tip.member.MemberService;
import com.web.tip.member.request.UpdateMemberRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = {"*"}, maxAge = 6000)
@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageController {
    private final MemberDetailService memberDetailService;
    private final MemberService memberService;

    private static final String SUCCESS = "success";

    @GetMapping
    public ResponseEntity<Object> getMypage(String memberId) {

        Member member = memberService.getMemberById(memberId);
        MemberDetailDto memberDetail = memberDetailService.findMemberDetail(member);

        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;
        result.object = memberDetail;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Object> updateMypage(UpdateMemberRequest updateMemberRequest){
        log.info("프로필 수정 요청이 들어왔습니다: " + updateMemberRequest.toString());
        MemberDetailDto memberDetail = memberDetailService.updateMemberDetail(updateMemberRequest);

        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;
        result.object = memberDetail;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
