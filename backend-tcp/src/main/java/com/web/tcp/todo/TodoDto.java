package com.web.tcp.todo;

import lombok.*;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoDto {

    @Id
    private String id;

    private String title;
    private String status;
    private String projectId;
    private String memberId;
    private String teamId;

    private LocalDateTime modifyDate;
    private LocalDateTime regDate;
}