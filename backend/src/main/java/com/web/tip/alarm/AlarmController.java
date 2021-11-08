package com.web.tip.alarm;

import com.web.tip.BasicResponse;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/alarm")
public class AlarmController {

    AlarmService alarmService;
    private static final String SUCCESS = "success";

    @PutMapping("/check")
    @ApiOperation(value = "알람 체크")
    public Object checkAlarm(@RequestBody List<String> checkList){
        log.info("알람 체크하기");

        alarmService.check(checkList);
        final BasicResponse result = new BasicResponse();

        result.status = true;
        result.data = SUCCESS;

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @PutMapping("/checkAll/{memberId}")
    @ApiOperation(value = "모든 알람 체크")
    public Object checkAllAlarm(@PathVariable(name = "memberId") String memberId){
        log.info("모든 알람 체크하기");

        alarmService.checkAll(memberId);
        final BasicResponse result = new BasicResponse();

        result.status = true;
        result.data = SUCCESS;

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @GetMapping("/userAlarmList/{mid}")
    @ApiOperation(value = "사용자 알람 목록")
    public Object getUserReview(@PathVariable("mid") String mid, final Pageable pageable) {

        log.info("알람 목록");
        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "success";

        Slice<Alarm> alarmPages = alarmService.getAlarmListByPages(mid, pageable);

        if (alarmPages.isEmpty()) {
            result.object = false;
        }
        else {
            result.object = alarmPages;
        }

        return result;
    }

    @GetMapping("/getAlarmCnt")
    @ApiOperation(value="현재 알람의 개수를 반환")
    public Object getReviewCnt(String mid) {
        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "success";

        long alarmCnt = alarmService.getAlarmCnt(mid);

        if (alarmCnt == 0) {
            result.object = false;
        }
        else {
            result.object = alarmCnt;
        }

        return result;
    }

}
