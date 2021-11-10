package com.web.tip.member.response;

import com.web.tip.mypage.MemberDetailDto;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponse {

    private String id;

    private String name;

    private MemberDetailDto memberDetailDto;

}
