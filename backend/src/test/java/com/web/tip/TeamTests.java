package com.web.tip;

import com.web.tip.member.Member;
import com.web.tip.team.CreationTeamRequest;
import com.web.tip.team.ModificationTeamRequest;
import com.web.tip.team.TeamDto;
import com.web.tip.team.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
public class TeamTests {

    @Autowired
    private TeamService teamService;

    @Test
    void testCreateTeam(){

        List<String> str = new ArrayList<>();
        str.add("1");
        str.add("2");

        Assertions.assertTrue(teamService.createProjectTeam(CreationTeamRequest.builder()
                        .projectId("1")
                        .teamName("테스트용")
                        .memberList(str)
                        .build()));

        List<TeamDto> teamList = (List<TeamDto>) teamService.getAllTeam("1");
        log.info(teamList.toString());

    }

    @Test
    void testModifyTeam(){

        List<String> str = new ArrayList<>();
        str.add("1");
        str.add("2");

        Assertions.assertTrue(teamService.modifyProjectTeamName(TeamDto.builder()
                        .id("0")
                        .name("디자인1")
                        .build()));

        List<TeamDto> teamList = (List<TeamDto>) teamService.getAllTeam("1");
        log.info(teamList.toString());

    }

    @Test
    void testModifyMember(){

        List<String> str = new ArrayList<>();
        str.add("1");
        str.add("2");

        Assertions.assertTrue(teamService.modifyProjectTeamMember(ModificationTeamRequest.builder()
                        .teamId("1")
                        .memberList(str)
                        .build()));

        List<TeamDto> teamList = (List<TeamDto>) teamService.getAllTeam("1");
        log.info(teamList.toString());

    }

    @Test
    void testDeleteMember(){
        Assertions.assertTrue(teamService.deleteProjectTeam("9973687821830"));
        log.info("done");

    }

}
