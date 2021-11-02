package com.web.tip.todo.content.url;

import com.web.tip.todo.content.TodoContent;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TodoUrl {
    @Id
    private String id;

    @Column(name = "url", length = 1000)
    private String url;

    @JoinColumn(name = "todo_content_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private TodoContent todoContent;

    @Column(name = "reg_date")
    private LocalDateTime regDate;
}
