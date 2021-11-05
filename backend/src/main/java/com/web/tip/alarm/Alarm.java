package com.web.tip.alarm;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Alarm {

    @Id
    private String id;

    private String content;
    private boolean isShow;
    private String memberId;
    private String todoId;

    public void changeIsShow(){
        this.isShow = !this.isShow;
    }
}
