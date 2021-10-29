package com.web.tip.mypage;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberDetailDto {
    private String email;
    private String phone;

    public static MemberDetailDto entityToDto(MemberDetail memberDetail){
        return MemberDetailDto.builder()
                .email(memberDetail.getEmail())
                .phone(memberDetail.getPhone())
                .build();
    }
}
