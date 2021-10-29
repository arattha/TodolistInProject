package com.web.tip.team;

import com.web.tip.project.Project;
import com.web.tip.project.ProjectDto;

public class TeamAdaptor {

    public static TeamDto entityToDto(Team team){

        return TeamDto.builder()
                .id(team.getId())
                .name(team.getName())
                .projectId(team.getProjectId())
                .isUse(team.isUse())
                .build();
    }

    public static Team dtoToEntity(TeamDto teamDto){

        return Team.builder()
                .id(teamDto.getId())
                .name(teamDto.getName())
                .projectId(teamDto.getProjectId())
                .isUse(teamDto.isUse())
                .build();
    }
}

