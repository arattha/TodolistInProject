package com.web.tcp.alarm;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class AlarmController {

    private final SimpMessagingTemplate template;

    AlarmService alarmService;

    // client가 '/server/getAlarm'경로로 member 아이디 전송
    // 해당 member(client)에게 send
    @MessageMapping(value = "/getAlarm")
    public void getAlarm(String memberId){

        try{
            memberId = (String) StringToJson(memberId).get("memberId");
            template.convertAndSend("/client/alarm/" + memberId, alarmService.getAlarmList(memberId));
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    // client가 '/server/checkAlarm'경로로 member 아이디와 alarm 아이디 전송
    // 해당 member(client)에게 send
    @MessageMapping(value = "/checkAlarm")
    public void checkAlarm(String memberId, String alarmId){

        try{
            alarmId = (String) StringToJson(alarmId).get("alarmId");
            memberId = (String) StringToJson(memberId).get("memberId");
            alarmService.checkAlarm(alarmId);
            template.convertAndSend("/client/alarm/" + memberId, alarmService.getAlarmList(memberId));
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    // client가 '/server/checkAll'경로로 member 아이디 전송
    // 해당 member(client)에게 send
    @MessageMapping(value = "/checkAll")
    public void checkAll(String memberId){

        try{
            memberId = (String) StringToJson(memberId).get("memberId");
            alarmService.checkAll(memberId);
            template.convertAndSend("/client/alarm/" + memberId, alarmService.getAlarmList(memberId));
        } catch(Exception e){
            e.printStackTrace();
        }
    }



    private JSONObject StringToJson(String str) throws ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(str);
        JSONObject jsonObject = (JSONObject) obj;

        return jsonObject;
    }
}
