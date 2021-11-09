package com.web.tcp.team;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Team {
    @Id
    private String id;
    private String name;
    private String projectId;
    private boolean isUse;

}
