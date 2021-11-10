package com.web.tip.team;

import lombok.*;

import javax.persistence.Id;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeamDto {

    private String id;
    private String name;
    private String projectId;

}
