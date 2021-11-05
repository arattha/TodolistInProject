package com.web.tip.alarm;

import com.web.tip.error.CustomException;
import com.web.tip.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    // 모든 알람 처리
    public void checkAll(String memberId) {

        try{
            System.out.println(memberId);
            List<Alarm> alarmList = alarmDao.findAlarmByMemberId(memberId);

            for(Alarm alarm : alarmList){
                System.out.println(alarm);
                alarm.changeIsShow();
                alarmDao.save(alarm);
            }

        } catch (Exception e){
            e.printStackTrace();
        }

    }

}
