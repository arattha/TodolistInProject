package com.web.tcp.todo;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Todo {

    @Id
    private String id;

    private String title;
    private String status;
    @Column(name = "project_id")
    private String projectId;
    private String member_id;
    private String team_id;

    private LocalDateTime modify_date;
    private LocalDateTime reg_date;

    public void changeBelong(String team_id, String member_id){
        this.team_id = team_id;
        this.member_id = member_id;
    }

    public void changeTitle(String title){
        this.title = title;
    }

    public void changeStatus(String status){
        this.status = status;
    }

    public void changeModifyDate(){
        this.modify_date = LocalDateTime.now();
    }
}
