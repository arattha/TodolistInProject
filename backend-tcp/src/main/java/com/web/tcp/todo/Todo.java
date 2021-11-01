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
    private String projectId;
    private String memberId;
    private String teamId;

    private LocalDateTime modifyDate;
    private LocalDateTime regDate;

    public void changeBelong(String teamId, String memberId){
        this.teamId = teamId;
        this.memberId = memberId;
    }

    public void changeTitle(String title){
        this.title = title;
    }

    public void changeStatus(String status){
        this.status = status;
    }

    public void changeModifyDate(){
        this.modifyDate = LocalDateTime.now();
    }
}
