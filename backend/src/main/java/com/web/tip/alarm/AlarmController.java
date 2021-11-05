package com.web.tip.alarm;

import com.web.tip.BasicResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/alarm")
public class AlarmController {

    AlarmService alarmService;
    private static final String SUCCESS = "success";

    @PutMapping("/check/{memberId}")
    @ApiOperation(value = "알람 체크")
    public Object checkAlarm(@RequestBody List<String> checkList, @PathVariable(name = "memberId") String memberId){
        log.info("알람 체크하기");

        alarmService.check(checkList);
        final BasicResponse result = new BasicResponse();

        result.status = true;
        result.data = SUCCESS;

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

}
