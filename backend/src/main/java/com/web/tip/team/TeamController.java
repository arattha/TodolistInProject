package com.web.tip.team;

import com.web.tip.BasicResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/team")
public class TeamController {

    TeamService teamService;

    @GetMapping
    @ApiOperation(value = "프로젝트 내 팀리스트 반환")
    public Object getProjectTeam(@RequestParam String projectName){

        List<Team> teamList = (List<Team>) teamService.getAllTeam(projectName);
        BasicResponse result = new BasicResponse();

        if(teamList != null){
            result.status = true;
            result.data = "success";
            result.object = teamList;
        } else {
            result.status = false;
            result.data = "fail";
            result.object = null;
        }


        return result;
    }

    @PostMapping
    @ApiOperation(value = "프로젝트 내에 새 팀 생성")
    public Object createProjectTeam(@RequestBody CreationTeamRequest newTeam){


        boolean flag = teamService.createProjectTeam(newTeam);
        BasicResponse result = new BasicResponse();

        if(flag){
            result.status = true;
            result.data = "success";
        } else {
            result.status = false;
            result.data = "fail";
        }

        return result;
    }


}
