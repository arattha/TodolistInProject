package com.web.tcp.alarm;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class AlarmService {

    AlarmDao alarmDao;

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
}
