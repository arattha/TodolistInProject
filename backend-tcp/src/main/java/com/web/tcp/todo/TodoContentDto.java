package com.web.tcp.todo;

import lombok.*;

@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoContentDto {
    private String todoId;
    private String memberId;
    private String contents;
}
