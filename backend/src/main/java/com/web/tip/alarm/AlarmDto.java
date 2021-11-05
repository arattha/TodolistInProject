package com.web.tip.alarm;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlarmDto {

    private String id;

    private String content;
    private boolean isShow;
    private String memberId;
    private String todoId;

}
