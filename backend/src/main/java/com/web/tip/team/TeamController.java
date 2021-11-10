package com.web.tip.team;

import com.web.tip.BasicResponse;
import com.web.tip.common.MemberHasTeamDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public Object getProjectTeam(@RequestParam String projectId){
        log.info("팀 리스트 목록 반환");

        List<TeamDto> teamList = (List<TeamDto>) teamService.getAllTeam(projectId);
        BasicResponse result = new BasicResponse();

        if(teamList != null){

            result.status = true;
            result.data = "success";
            result.object = teamList;

            return new ResponseEntity<>(result, HttpStatus.OK);

        } else {
            result.status = false;
            result.data = "fail";
            result.object = null;

            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/members")
    @ApiOperation(value = "프로젝트 내 멤버리스트 반환")
    public Object getProjectMembers(@RequestParam String projectId) {
        log.info("프로젝트 내 멤버리스트 반환");

        List<MemberHasTeamDto> teamList = (List<MemberHasTeamDto>) teamService.getAllMembers(projectId);
        BasicResponse result = new BasicResponse();

        if (!teamList.isEmpty()) {

            result.status = true;
            result.data = "success";
            result.object = teamList;

            return new ResponseEntity<>(result, HttpStatus.OK);

        } else {
            result.status = false;
            result.data = "fail";
            result.object = null;

            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/ncheck")
    @ApiOperation(value = "프로젝트 내 멤버리스트 반환")
    public Object teamNameCheck(@RequestParam String projectId, @RequestParam String teamName) {
        log.info("프로젝트 내 이름 중복 체크");

        boolean check = teamService.teamNameCheck(projectId,teamName);
        BasicResponse result = new BasicResponse();

        if (check) {

            result.status = true;
            result.data = "success";
            result.object = null;

            return new ResponseEntity<>(result, HttpStatus.OK);

        } else {
            result.status = false;
            result.data = "fail";
            result.object = null;

            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    @PostMapping
    @ApiOperation(value = "프로젝트 내 새 팀 생성")
    public Object createProjectTeam(@RequestBody CreationTeamRequest newTeam){
        log.info("새 팀 생성");

        boolean flag = teamService.createProjectTeam(newTeam);
        BasicResponse result = new BasicResponse();

        if(flag){
            result.status = true;
            result.data = "success";

        } else {
            result.status = false;
            result.data = "fail";

        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping
    @ApiOperation(value = "프로젝트 내 팀 멤버 수정")
    public Object modifyProjectTeamMember(@RequestBody ModificationTeamRequest modifyTeam){
        log.info("팀 멤버 수정");

        boolean flag = teamService.modifyProjectTeamMember(modifyTeam);
        BasicResponse result = new BasicResponse();

        if(flag){
            result.status = true;
            result.data = "success";

        } else {
            result.status = false;
            result.data = "fail";

        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping
    @ApiOperation(value = "팀 이름 수정")
    public Object modifyProjectTeamName(@RequestBody TeamDto teamDto){
        log.info("팀 이름 수정");

        boolean flag = teamService.modifyProjectTeamName(teamDto);
        BasicResponse result = new BasicResponse();

        if(flag){
            result.status = true;
            result.data = "success";

        } else {
            result.status = false;
            result.data = "fail";

        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @DeleteMapping
    @ApiOperation(value = "프로젝트 내 팀 삭제")
    public Object deleteProjectTeam(@RequestParam String teamId){
        log.info("팀 삭제");

        boolean flag = teamService.deleteProjectTeam(teamId);
        BasicResponse result = new BasicResponse();

        if(flag) {
            result.status = true;
            result.data = "success";

        } else {
            result.status = false;
            result.data = "fail";

        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
