package com.web.tip.alarm;

import com.web.tip.error.CustomException;
import com.web.tip.error.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmDao alarmDao;

    // 체크된 알람 처리
    public void check(List<String> checkList) {

        Alarm alarm;
        for(String alarmId : checkList){
            alarm = alarmDao.findAlarmById(alarmId).orElseThrow(() -> new CustomException(ErrorCode.ALARM_NOT_FOUND));
            alarm.changeIsShow();
            alarmDao.save(alarm);
        }

    }

}
