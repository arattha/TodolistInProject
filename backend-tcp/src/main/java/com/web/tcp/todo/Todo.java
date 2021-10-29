package com.web.tcp.todo;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Setter
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
    private String project_id;
    private String member_id;
    private String team_id;

    private LocalDateTime modify_date;
    private LocalDateTime reg_date;
}
