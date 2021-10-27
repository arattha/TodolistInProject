package com.web.tip.project;

import com.web.tip.BasicResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/project")
public class ProjectController {

    ProjectService projectService;

    // 프로젝트 목록 반환
    @GetMapping("/getProjectList")
    @ApiOperation(value = "프로젝트 목록 반환")
    public Object getProject(@RequestParam String nickname){
        log.info("프로젝트 목록 반환");

        Optional<Project> projectOpt = projectService.getProjectList(nickname);
        BasicResponse result = new BasicResponse();

        if(projectOpt.isPresent()){

            result.status = true;
            result.data = "success";
            result.object = projectOpt.get();

        } else {

            result.status = true;
            result.data = "success";
            result.object = null;

        }

        return result;

    }
}
