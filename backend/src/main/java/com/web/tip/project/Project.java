package com.web.tip.project;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Project {

    @Id
    private String id;

    private String name;
    private String desc;

    private boolean isDone;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Column(insertable = false)
    private LocalDateTime regDate;

}
