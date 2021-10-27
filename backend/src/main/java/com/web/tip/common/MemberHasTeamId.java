package com.web.tip.common;

import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.io.Serializable;

@NoArgsConstructor
public class MemberHasTeamId implements Serializable {

    @Id
    private String memberId;
    @Id
    private String teamId;

}
