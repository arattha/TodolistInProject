package com.web.tip.todo;

import com.web.tip.member.Member;
import com.web.tip.project.Project;
import com.web.tip.team.Team;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Todo {
    @Id
    private String id;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "status", length = 45)
    private String status;

    @JoinColumn(name = "project_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @JoinColumn(name = "team_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(name = "modify_date")
    private LocalDateTime modifyDate;

    @Column(name = "reg_date")
    private LocalDateTime regDate;
}
