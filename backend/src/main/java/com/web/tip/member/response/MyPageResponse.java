package com.web.tip.member.response;

import com.web.tip.member.Member;
import com.web.tip.member.security.Authority;
import com.web.tip.mypage.MemberDetailDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class MyPageResponse {
    private String name;
    private String nickName;
    private Authority authority;
    private String email;
    private String phone;

    public static MyPageResponse entityToResponse(Member member, MemberDetailDto memberDetailDto){
        return MyPageResponse.builder()
                .name(member.getName())
                .nickName(member.getNickname())
                .authority(member.getAuthority())
                .email(memberDetailDto.getEmail())
                .phone(memberDetailDto.getPhone())
                .build();
    }
}
