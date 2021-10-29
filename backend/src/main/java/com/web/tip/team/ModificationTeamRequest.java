package com.web.tip.team;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ModificationTeamRequest {
    String teamId;
    List<String> memberList;
}
