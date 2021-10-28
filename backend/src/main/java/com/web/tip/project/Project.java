package com.web.tip.project;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    private String id;

    private String name;
    @Column(name = "`desc`")
    private String desc;

    private boolean isDone;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime regDate;

}
