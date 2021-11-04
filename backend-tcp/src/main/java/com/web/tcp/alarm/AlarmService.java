package com.web.tcp.alarm;

import com.web.tcp.bookmark.BookmarkDao;
import com.web.tcp.error.CustomException;
import com.web.tcp.error.ErrorCode;
import com.web.tcp.todo.Todo;
import com.web.tcp.todo.TodoDao;
import com.web.tcp.util.IdGenerator;
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
    TodoDao todoDao;
    BookmarkDao bookmarkDao;

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
    public boolean checkAlarm(String alarmId) {

        try{
            Alarm alarm = alarmDao.findAlarmById(alarmId).orElseThrow(() -> new CustomException(ErrorCode.ALARM_NOT_FOUND));
            alarm.changeIsShow();

            alarmDao.save(alarm);
            return true;
        } catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Transactional
    public boolean checkAll(String memberId) {

        try{
            List<Alarm> alarmList = alarmDao.findAlarmByMemberId(memberId);

            for(Alarm alarm : alarmList){
                alarm.changeIsShow();
                alarmDao.save(alarm);
            }

            return true;
        } catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Transactional
    public boolean addAlarm(String content, String todoId) {

        IdGenerator idGenerator = new IdGenerator();
        String aid = idGenerator.generateId();

        try {
            // alarm은 담당자와 즐겨찾기한 모든 member에게 추가되어야 함
            // 1. 북마크에 해당 todo와 연결된 모든 member들에게 alarm 추가
            // 2. 1에서 얻은 member 목록에 담당자가 없다면 따로 담당자에게 alarm 추가 기능 구현

            Todo todo = todoDao.findTodoById(todoId).orElseThrow(() -> new CustomException(ErrorCode.TODO_NOT_FOUND));
            String manageMemberId = todo.getMemberId();
            boolean isInclude = false;      // memberList에 manageMemberId가 포함되어 있는지 확인할 변수

            List<String> memberList = bookmarkDao.findMemberIdByTodoId(todoId);
            log.info("member" + memberList);

            for (String memberId : memberList) {

                if(memberId.equals(manageMemberId)) isInclude = true;

                while (alarmDao.existsById(aid)) {
                    aid = idGenerator.generateId();
                }
                Alarm alarm = Alarm.builder()
                        .id(aid)
                        .content(content)
                        .isShow(false)
                        .memberId(memberId)
                        .todoId(todoId)
                        .build();

                alarmDao.save(alarm);
            }

            // 담당자가 북마크된 memberList에 없으므로 따로 추가해준다.
            if(!isInclude){
                while (alarmDao.existsById(aid)) {
                    aid = idGenerator.generateId();
                }
                Alarm alarm = Alarm.builder()
                        .id(aid)
                        .content(content)
                        .isShow(false)
                        .memberId(manageMemberId)
                        .todoId(todoId)
                        .build();

                alarmDao.save(alarm);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
