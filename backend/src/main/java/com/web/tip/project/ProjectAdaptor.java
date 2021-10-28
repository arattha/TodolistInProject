package com.web.tip.project;

public class ProjectAdaptor {

    public static ProjectDto entityToDto(Project project){

        return ProjectDto.builder()
                .id(project.getId())
                .name(project.getName())
                .desc(project.getDesc())
                .isDone(project.isDone())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .regDate(project.getRegDate())
                .build();
    }

    public static Project dtoToEntity(ProjectDto projectDto){

        return Project.builder()
                .id(projectDto.getId())
                .name(projectDto.getName())
                .desc(projectDto.getDesc())
                .isDone(projectDto.isDone())
                .startDate(projectDto.getStartDate())
                .endDate(projectDto.getEndDate())
                .regDate(projectDto.getRegDate())
                .build();
    }
}
