package com.web.tip.mypage;

import com.web.tip.error.CustomException;
import com.web.tip.error.ErrorCode;
import com.web.tip.image.ImgFileDto;
import com.web.tip.image.ImgService;
import com.web.tip.member.Member;
import com.web.tip.member.MemberDao;
import com.web.tip.member.request.SignUpRequest;
import com.web.tip.member.request.UpdateMemberRequest;
import com.web.tip.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberDetailService {
    private final MemberDao memberDao;
    private final MemberDetailDao memberDetailDao;
    private final IdGenerator idGenerator;
    private final ImgService imgService;

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

    public MemberDetailDto updateMemberDetail(UpdateMemberRequest updateMemberRequest) {
        Member member = memberDao.findMemberById(updateMemberRequest.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Optional.ofNullable(updateMemberRequest.getMultipartFile())
                .ifPresent(multipartFile -> {
                    try {
                        imgService.addFile(ImgFileDto.builder()
                                .id(member.getId())
                                .multipartFile(multipartFile)
                                .build()
                        );
                    } catch (IOException e) {
                        log.error("saving profile Image failed");
                        throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
                    }
                });

        MemberDetail memberDetail = memberDetailDao.findMemberDetailByMemberId(updateMemberRequest.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        MemberDetail updatedMember = MemberDetail.builder()
                .member(member)
                .id(memberDetail.getId())
                .phone(updateMemberRequest.getPhone() == null ? memberDetail.getPhone() : updateMemberRequest.getPhone())
                .email(updateMemberRequest.getEmail() == null ? memberDetail.getEmail() : updateMemberRequest.getEmail())
                .profileImg(memberDetail.getProfileImg())
                .build();

        memberDetailDao.save(updatedMember);

        return MemberDetailDto.entityToDto(updatedMember);
    }

    public MemberDetailDto findMemberDetail(Member member) {
        return MemberDetailDto.entityToDto(
                memberDetailDao.findMemberDetailByMember(member)
                        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)));
    }


}
