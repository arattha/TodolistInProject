package com.web.tip.bookmark;

import lombok.*;
import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bookmark {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "member_id")
    private String memberId;

    @Column(name = "todo_id")
    private String todoId;

    @Column(name = "is_use")
    private boolean isUse;

    public void deleteBookmark(){
        this.isUse = false;
    }

}