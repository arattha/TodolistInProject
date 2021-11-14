package com.web.tcp.common;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@Builder
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

    private boolean isUse;

}
