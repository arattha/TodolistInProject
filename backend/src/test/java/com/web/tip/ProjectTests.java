package com.web.tip;

import com.web.tip.member.MemberService;
import com.web.tip.member.request.SignUpRequest;
import com.web.tip.project.Project;
import com.web.tip.project.ProjectDto;
import com.web.tip.project.ProjectService;
import com.web.tip.util.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.channels.MembershipKey;
import java.time.LocalDateTime;

@Slf4j
@SpringBootTest
public class ProjectTests {

    @Autowired
    private ProjectService projectService;

    @Test
    void createProject(){
        Assertions.assertTrue(projectService.addProject(ProjectDto.builder()
                        .name("test_project")
                        .desc("this is test for creating project")
                        .isDone(false)
                        .startDate(LocalDateTime.now())
                        .endDate(LocalDateTime.now())
                        .regDate(LocalDateTime.now())
                        .build()
                ));

        Project project = projectService.getProjectByProjectName("test_project");
        log.info(project.toString());
        Assertions.assertTrue(projectService.existsProjectCheck("test_project"));
    }

    @Test
    void changeDoneProject(){
        Assertions.assertTrue(projectService.finishProject("7400990673069"));
        Project project = projectService.getProjectByProjectName("음악");
        log.info(project.toString());
    }

}
