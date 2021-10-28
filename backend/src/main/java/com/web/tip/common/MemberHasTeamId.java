package com.web.tip.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MemberHasTeamId implements Serializable {

    @Id
    private String memberId;
    @Id
    private String teamId;

}
