package com.web.tip.team;

import com.web.tip.project.Project;
import com.web.tip.project.ProjectDto;

public class TeamAdaptor {

    public static TeamDto entityToDto(Team team){

        return TeamDto.builder()
                .id(team.getId())
                .name(team.getName())
                .projectId(team.getProjectId())
                .build();
    }
}

