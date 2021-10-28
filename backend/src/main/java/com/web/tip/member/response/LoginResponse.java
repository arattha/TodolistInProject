package com.web.tip.member.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    String mid;
    String nickname;
    boolean isMember;
}
