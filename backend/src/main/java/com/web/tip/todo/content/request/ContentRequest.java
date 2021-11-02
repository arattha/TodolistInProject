package com.web.tip.todo.content.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ContentRequest {
    private String todoId;
    private String memberId;
    private String contents;
}
