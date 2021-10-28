package com.web.tip.member.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class SignUpRequest {
    String password;
    String nickname;
    String name;
//    String email;
//    String phone;
}
