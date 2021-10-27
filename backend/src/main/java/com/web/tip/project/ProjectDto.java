package com.web.tip.project;

import lombok.*;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {

    private String name;
    private String desc;

    private boolean isDone;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime regDate;

}
