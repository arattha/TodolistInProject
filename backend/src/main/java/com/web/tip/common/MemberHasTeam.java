package com.web.tip.common;

import lombok.*;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@IdClass(MemberHasTeamId.class)
public class MemberHasTeam {

    @Id
    private String memberId;
    @Id
    private String teamId;

}
