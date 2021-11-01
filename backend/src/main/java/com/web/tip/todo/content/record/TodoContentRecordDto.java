package com.web.tip.todo.content.record;

import com.web.tip.todo.content.TodoContent;
import com.web.tip.todo.content.url.TodoUrl;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Builder
@ToString
public class TodoContentRecordDto {
    private String id;

    private Map<String, String> diff;

    private LocalDateTime modifyDate;

    public static TodoContentRecordDto entityToDto(TodoContentRecord todoContentRecord){
        return TodoContentRecordDto.builder()
                .id(todoContentRecord.getId())
                .diff(todoContentRecord.getDiff())
                .modifyDate(todoContentRecord.getModifyDate())
                .build();
    }
}
