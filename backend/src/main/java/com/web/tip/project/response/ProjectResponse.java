package com.web.tip.project.response;

import com.web.tip.project.Project;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {
    private String id;
    private String name;
    private String desc;
    private LocalDate startDate;
    private LocalDate endDate;

    private int totalCnt;
    private int progressCnt;
    private int doneCnt;

    public static ProjectResponse entityToResponse(Project project, int[] count){
        return ProjectResponse.builder()
                .id(project.getId())
                .desc(project.getDesc())
                .name(project.getName())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .totalCnt(count[0])
                .progressCnt(count[1])
                .doneCnt(count[2])
                .build();
    }
}
