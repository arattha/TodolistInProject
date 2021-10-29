package com.web.tip.mypage;

import com.web.tip.error.CustomException;
import com.web.tip.error.ErrorCode;
import com.web.tip.member.Member;
import com.web.tip.member.MemberDao;
import com.web.tip.member.request.SignUpRequest;
import com.web.tip.member.request.UpdateMemberRequest;
import com.web.tip.member.response.MyPageResponse;
import com.web.tip.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailService {
    private final MemberDao memberDao;
    private final MemberDetailDao memberDetailDao;
    private final IdGenerator idGenerator;

    public void createMemberDetail(Member member, SignUpRequest signUpRequest) {
        String id = idGenerator.generateId();
        while (memberDetailDao.existsById(id)) {
            id = idGenerator.generateId();
        }

        MemberDetail memberDetail = MemberDetail.builder()
                .member(member)
                .id(id)
                .email(signUpRequest.getEmail())
                .phone(signUpRequest.getPhone())
                .build();

        memberDetailDao.save(memberDetail);
    }

    public MyPageResponse updateMemberDetail(UpdateMemberRequest updateMemberRequest) {
        Member member = memberDao.findMemberById(updateMemberRequest.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        MemberDetail memberDetail = memberDetailDao.findMemberDetailByMember(member)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        MemberDetail updatedMember = MemberDetail.builder()
                .member(member)
                .id(memberDetail.getId())
                .phone(updateMemberRequest.getPhone()==null? memberDetail.getPhone() : updateMemberRequest.getPhone())
                .email(updateMemberRequest.getEmail()==null? memberDetail.getEmail() : updateMemberRequest.getEmail())
                .build();

        memberDetailDao.save(updatedMember);

        return MyPageResponse.entityToResponse(member, MemberDetailDto.entityToDto(updatedMember));
    }

    public MemberDetailDto findMemberDetail(Member member) {
        return MemberDetailDto.entityToDto(
                memberDetailDao.findMemberDetailByMember(member)
                        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)));
    }


}
