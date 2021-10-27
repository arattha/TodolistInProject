package com.web.tip.team;


import com.web.tip.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreationTeamRequest {

    String projectId;
    String teamName;
    List<Member> memberList;

}
