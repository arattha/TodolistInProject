package com.web.tip.project.request;

import com.web.tip.project.Project;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequest {
    private String id;
    private String name;
    private String desc;
    private LocalDate startDate;
    private LocalDate endDate;
}
