package com.web.tcp.bookmark;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bookmark {

    @Id
    private String id;

    private String memberId;
    private String todoId;
    private boolean isUse;

    public void changeUse(){
        this.isUse = !this.isUse;
    }
}
