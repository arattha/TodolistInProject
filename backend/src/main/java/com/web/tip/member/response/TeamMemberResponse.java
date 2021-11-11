package com.web.tip.member.response;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamMemberResponse {
    private String id;

    private String name;

    private String email;
}
