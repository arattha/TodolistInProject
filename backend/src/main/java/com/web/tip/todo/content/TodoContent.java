package com.web.tip.todo.content;

import com.web.tip.member.Member;
import com.web.tip.todo.Todo;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TodoContent {
    @Id
    private String id;

    @Column(name = "contents", nullable = false, length = 1000)
    private String contents;

    @Column(name ="is_use")
    private boolean isUse;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id")
    private Todo todo;

    @Column(name = "reg_date")
    private LocalDateTime regDate;
}
