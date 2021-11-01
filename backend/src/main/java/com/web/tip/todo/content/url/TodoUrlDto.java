package com.web.tip.todo.content.url;

import com.web.tip.todo.content.TodoContent;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
public class TodoUrlDto {
    private String id;

    private String url;

    private String writer;

    private LocalDateTime regDate;

    public static TodoUrlDto entityToDto(TodoUrl todoUrl, String writer){
        return TodoUrlDto.builder()
                .id(todoUrl.getId())
                .url(todoUrl.getUrl())
                .writer(writer)
                .regDate(todoUrl.getRegDate())
                .build();
    }
}
