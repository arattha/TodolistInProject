package com.web.tip.todo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.Map;

@Getter
@Setter
@Builder
@ToString
public class TodoRecordDto {
    private String id;
    private Map<String, String> diff;

    public static TodoRecordDto entityToDto(TodoRecord todoRecord){
        return TodoRecordDto.builder()
                .id(todoRecord.getId())
                .diff(todoRecord.getDiff())
                .build();
    }
}
