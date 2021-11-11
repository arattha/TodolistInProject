package com.web.tip.todo.content;

import com.web.tip.member.MemberDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
public class TodoContentDto {
    private String id;

    private String contents;

    private String writer;

    private String writerId;

    private LocalDateTime regDate;

    public static TodoContentDto entityToDto(TodoContent todoContent) {
        return TodoContentDto.builder()
                .id(todoContent.getId())
                .contents(todoContent.getContents())
                .writer(todoContent.getMember().getName())
                .writerId(todoContent.getMember().getId())
                .regDate(todoContent.getRegDate())
                .build();
    }
}
