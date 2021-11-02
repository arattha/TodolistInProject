package com.web.tcp.alarm;

import com.web.tcp.error.CustomException;
import com.web.tcp.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class AlarmService {

    AlarmDao alarmDao;

    @Transactional
    public List<AlarmDto> getAlarmList(String memberId) {

        List<AlarmDto> alarmDtoList = new ArrayList<>();

        try{

            List<Alarm> alarmList = alarmDao.findAlarmByMemberId(memberId);
            AlarmDto alarmDto;

            for(Alarm alarm : alarmList){

                if(!alarm.isShow()){
                    alarmDto = AlarmDto.builder()
                            .id(alarm.getId())
                            .content(alarm.getContent())
                            .memberId(alarm.getMemberId())
                            .todoId(alarm.getTodoId())
                            .build();
                    alarmDtoList.add(alarmDto);
                }

            }


        } catch (Exception e){
            e.printStackTrace();
        }

        return alarmDtoList;
    }

    @Transactional
    public void checkAlarm(String alarmId) {

        Alarm alarm = alarmDao.findAlarmById(alarmId).orElseThrow(() -> new CustomException(ErrorCode.ALARM_NOT_FOUND));
        alarm.changeIsShow();

        alarmDao.save(alarm);

    }



}
