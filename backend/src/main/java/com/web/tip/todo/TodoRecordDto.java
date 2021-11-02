package com.web.tip.todo;

import com.web.tip.todo.content.record.TodoContentRecord;
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
public class TodoRecordDto {
    private String id;

    private boolean isContent;
    // writer, before, after, message
    private Map<String, String> diff;

    private LocalDateTime modifyDate;

    public static TodoRecordDto todoRecordToDto(TodoRecord todoRecord){
        return TodoRecordDto.builder()
                .id(todoRecord.getId())
                .isContent(false)
                .modifyDate(todoRecord.getModifyDate())
                .diff(todoRecord.getDiff())
                .build();
    }

    public static TodoRecordDto todoContentRecordToDto(TodoContentRecord todoContentRecord){
        return TodoRecordDto.builder()
                .id(todoContentRecord.getId())
                .isContent(true)
                .modifyDate(todoContentRecord.getModifyDate())
                .diff(todoContentRecord.getDiff())
                .build();
    }
}
