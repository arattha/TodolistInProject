package com.web.tip.common;

import lombok.*;
import javax.persistence.*;

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

}
