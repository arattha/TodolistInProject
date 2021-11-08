package com.web.tip.alarm;

import com.web.tip.error.CustomException;
import com.web.tip.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmDao alarmDao;

    // 체크된 알람 처리
    public void check(List<String> checkList) {
        System.out.println(checkList);
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
            List<Alarm> alarmList = alarmDao.findAlarmByMemberId(memberId);

            for(Alarm alarm : alarmList){
                alarm.changeIsShow();
                alarmDao.save(alarm);
            }

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public Slice<Alarm> getAlarmListByPages(String mid, Pageable pageable) {
        Slice<Alarm> alarmPage = null;

        try {
            alarmPage = alarmDao.findAlarmByMemberIdAndIsShowIsFalse(mid, pageable);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.ALARM_NOT_FOUND);
        }

        return alarmPage;
    }

    public long getAlarmCnt(String mid) {
        long cnt = 0l;

        try {
            cnt = alarmDao.countAlarmByMemberIdAndIsShowIsFalse(mid);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.ALARM_NOT_FOUND);
        }

        return cnt;
    }
}
