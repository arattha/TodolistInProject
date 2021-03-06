package com.web.tcp.alarm;

import com.web.tcp.bookmark.Bookmark;
import com.web.tcp.bookmark.BookmarkDao;
import com.web.tcp.error.CustomException;
import com.web.tcp.error.ErrorCode;
import com.web.tcp.member.Member;
import com.web.tcp.member.MemberDao;
import com.web.tcp.todo.Todo;
import com.web.tcp.todo.TodoDao;
import com.web.tcp.util.IdGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class AlarmService {

    AlarmDao alarmDao;
    MemberDao memberDao;
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
                            .regDate(alarm.getRegDate())
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
    public List<String> addAlarm(String content, String todoId) {

        IdGenerator idGenerator = new IdGenerator();
        String aid = idGenerator.generateId();

        try {
            // alarm??? ???????????? ??????????????? ?????? member?????? ??????????????? ???
            // 1. ???????????? ?????? todo??? ????????? ?????? member????????? alarm ??????
            // 2. 1?????? ?????? member ????????? ???????????? ????????? ?????? ??????????????? alarm ?????? ?????? ??????

            Todo todo = todoDao.findTodoById(todoId).orElseThrow(() -> new CustomException(ErrorCode.TODO_NOT_FOUND));

            Member manageMember = memberDao.findMemberById(todo.getMemberId()).orElse(null);

            boolean isInclude = false;      // memberList??? manageMemberId??? ???????????? ????????? ????????? ??????

            List<Bookmark> memberList = bookmarkDao.findBookmarkByTodoIdAndIsUseIsTrue(todoId);
            List<String> memberIdList = new ArrayList<>();
            memberList.forEach(v -> memberIdList.add(v.getMemberId()));

            for (String memberId : memberIdList) {

                if(manageMember != null && memberId.equals(manageMember.getId())) isInclude = true;

                while (alarmDao.existsById(aid)) {
                    aid = idGenerator.generateId();
                }
                Alarm alarm = Alarm.builder()
                        .id(aid)
                        .content(content)
                        .isShow(false)
                        .memberId(memberId)
                        .todoId(todoId)
                        .regDate(LocalDateTime.now())
                        .build();

                alarmDao.save(alarm);
            }

            // ???????????? ???????????? memberList??? ???????????? ?????? ???????????????.
            if(manageMember != null && !isInclude){
                while (alarmDao.existsById(aid)) {
                    aid = idGenerator.generateId();
                }
                Alarm alarm = Alarm.builder()
                        .id(aid)
                        .content(content)
                        .isShow(false)
                        .memberId(manageMember.getId())
                        .todoId(todoId)
                        .regDate(LocalDateTime.now())
                        .build();

                alarmDao.save(alarm);

                memberIdList.add(manageMember.getId());
            }

            return memberIdList;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
