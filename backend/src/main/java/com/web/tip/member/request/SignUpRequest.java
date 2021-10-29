package com.web.tip.member.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class SignUpRequest {
    private String password;
    private String nickname;
    private String name;
    private String email;
    private String phone;
}
