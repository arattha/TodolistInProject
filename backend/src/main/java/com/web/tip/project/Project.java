package com.web.tip.project;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Project {

    @Id
    private String id;

    private String name;
    @Column(name = "`desc`")
    private String desc;

    private boolean isDone;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime regDate;

    public void changeProjectState(){
        this.isDone = !this.isDone;
    }

    public void changeInfo(String name, String desc, LocalDate startDate, LocalDate endDate ){
        this.name = name;
        this.desc = desc;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
