package com.web.tcp;

import com.web.tcp.alarm.AlarmService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class AlarmTests {

    @Autowired
    AlarmService alarmService;

    // DB alarm Table에 id가 1231231231231 인 row가 존재해야 함
    // 해당 row의 isShow값이 false(0) 이어야 getAlarmList Test 통과

    @Test
    public void addAlarm(){
        Assertions.assertTrue(alarmService.addAlarm("로그인 : A님께서 상태를 접수에서 진행(으)로 변경했습니다.", "1265706059665"), "알람 추가 실패");
    }

    @Test
    public void checkAlarm(){
        Assertions.assertTrue(alarmService.checkAlarm("1231231231231"), "알람 체크 실패");
    }

    @Test
    public void getAlarmList(){
        Assertions.assertTrue(alarmService.getAlarmList("1231231231231").size() == 1, "알람 목록 반환 실패");
    }
}
