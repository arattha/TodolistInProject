package com.web.tip;

import com.web.tip.member.Member;
import com.web.tip.member.MemberDao;
import com.web.tip.project.Project;
import com.web.tip.project.ProjectDao;
import com.web.tip.team.Team;
import com.web.tip.team.TeamDao;
import com.web.tip.todo.Todo;
import com.web.tip.todo.TodoDao;
import com.web.tip.todo.TodoRecordDao;
import com.web.tip.todo.content.TodoContentDao;
import com.web.tip.todo.content.record.TodoContentRecordDao;
import com.web.tip.todo.content.url.TodoUrlDao;
import com.web.tip.util.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@Slf4j
@SpringBootTest
public class TodoTest {
    @Autowired
    private TodoDao todoDao;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private TeamDao teamDao;

    @Test
    void testTodo(){
        Member member = memberDao.findMemberByNickname("test")
                .get();
        Project project = projectDao.findProjectByName("test_project")
                .get();
        Team team = teamDao.findTeamByProjectId(project.getId())
                .get(0);

        Todo todo = Todo.builder()
                .id(idGenerator.generateId())
                .member(member)
                .modifyDate(LocalDateTime.now())
                .status("진행중")
                .project(project)
                .team(team)
                .title("test")
                .build();

        Todo savedTodo = todoDao.save(todo);
        Assertions.assertEquals(savedTodo.getId(), todo.getId());
    }
}
