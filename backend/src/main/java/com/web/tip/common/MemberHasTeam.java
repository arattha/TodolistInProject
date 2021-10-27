package com.web.tip.common;

import lombok.*;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberHasTeam {

    private String memberId;
    private String teamId;

}
