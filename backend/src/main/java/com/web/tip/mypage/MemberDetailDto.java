package com.web.tip.mypage;

import com.web.tip.member.Member;
import com.web.tip.member.security.Authority;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberDetailDto {
    private String name;
    private String nickname;
    private Authority authority;
    private String email;
    private String phone;
    private String profileImg;

    public static MemberDetailDto entityToDto(MemberDetail memberDetail) {
        Member member = memberDetail.getMember();

        return MemberDetailDto.builder()
                .name(member.getName())
                .nickname(member.getNickname())
                .authority(member.getAuthority())
                .email(memberDetail.getEmail())
                .phone(memberDetail.getPhone())
                .profileImg(memberDetail.getProfileImg())
                .build();
    }
}
