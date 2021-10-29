package com.web.tip.member.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class UpdateMemberRequest {
    private String id;
    private String email;
    private String phone;
}
