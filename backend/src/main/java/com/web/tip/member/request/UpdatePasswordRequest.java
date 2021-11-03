package com.web.tip.member.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class UpdatePasswordRequest {
    String id;
    String password;
}
