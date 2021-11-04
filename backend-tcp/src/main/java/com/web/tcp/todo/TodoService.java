package com.web.tcp.todo;

import com.web.tcp.alarm.AlarmService;
import com.web.tcp.error.CustomException;
import com.web.tcp.error.ErrorCode;
import com.web.tcp.member.Member;
import com.web.tcp.member.MemberDao;
import com.web.tcp.todoRecord.TodoRecord;
import com.web.tcp.todoRecord.TodoRecordDao;
import com.web.tcp.util.IdGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class TodoService {

    MemberDao memberDao;
    TodoDao todoDao;
    TodoRecordDao todoRecordDao;
    AlarmService alarmService;

    @Transactional
    public boolean addTodo(TodoDto todoDto) {

        try{

            IdGenerator idGenerator = new IdGenerator();
            String tid = idGenerator.generateId();
            while(todoDao.existsById(tid)){
                tid = idGenerator.generateId();
            }

            Todo todo = Todo.builder()
                    .id(tid)
                    .title(todoDto.getTitle())
                    .status(todoDto.getStatus())
                    .projectId(todoDto.getProjectId())
                    .teamId(todoDto.getTeamId())
                    .memberId(todoDto.getMemberId())
                    .modifyDate(todoDto.getModifyDate())
                    .regDate(todoDto.getRegDate())
                    .build();

            todoDao.save(todo);

            String todoRecordId = idGenerator.generateId();
            while(todoRecordDao.existsById(todoRecordId)){
                todoRecordId = idGenerator.generateId();
            }

            Map<String, String> diff = new HashMap<>();
            diff.put("message", todo.getTitle() + "이(가) 생성되었습니다.");
            TodoRecord todoRecord = TodoRecord.builder()
                    .id(todoRecordId)
                    .diff(diff)
                    .todo_id(todo.getId())
                    .modifyDate(LocalDateTime.now())
                    .build();

            todoRecordDao.save(todoRecord);
            return true;

        } catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }

    @Transactional
    public Object getTodoList(String projectId) {

        List<TodoDto> todoDtoList = new ArrayList<>();
        try{
            TodoDto todoDto = new TodoDto();

            List<Todo> todoList = todoDao.findTodosByProjectId(projectId);
            for(Todo todo : todoList) {
                Member member = memberDao.findMemberById(todo.getMemberId()).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
                todoDto = todoDto.builder()
                        .id(todo.getId())
                        .title(todo.getTitle())
                        .status(todo.getStatus())
                        .projectId(todo.getProjectId())
                        .teamId(todo.getTeamId())
                        .memberId(todo.getMemberId())
                        .memberName(member.getName())
                        .modifyDate(todo.getModifyDate())
                        .regDate(todo.getRegDate())
                        .build();

                todoDtoList.add(todoDto);
            }

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }

        return todoDtoList;
    }

    @Transactional
    public Todo getTodo(String todoTitle) {

        Todo todo = todoDao.findTodoByTitle(todoTitle).orElseThrow(() -> new CustomException(ErrorCode.TODO_NOT_FOUND));
        return todo;

    }

    // moveTodoStatus, moveTodoTeam, moveTodoMember
    // 해당 Todo의 변경된 데이터를 DB에 반영하고
    // 이를 todoRecord DB에 저장
    @Transactional
    public boolean moveTodoStatus(TodoDto todoDto) {

        try {

            String todoId = todoDto.getId();
            Todo todo = todoDao.findTodoById(todoId).orElseThrow(() -> new CustomException(ErrorCode.TODO_NOT_FOUND));
            Todo todoTmp = Todo.builder()
                    .id(todo.getId())
                    .title(todo.getTitle())
                    .status(todo.getStatus())
                    .projectId(todo.getProjectId())
                    .teamId(todo.getTeamId())
                    .memberId(todo.getMemberId())
                    .modifyDate(todo.getModifyDate())
                    .regDate(todo.getRegDate())
                    .build();

            IdGenerator idGenerator = new IdGenerator();
            String todoRecordId = idGenerator.generateId();
            while(todoRecordDao.existsById(todoRecordId)){
                todoRecordId = idGenerator.generateId();
            }

            // todo변경 시 diff에 저장
            Map<String, String> diff = new HashMap<>();

            Member member = memberDao.findMemberById(todoTmp.getMemberId()).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
            String writer = member.getName();

            todo.changeStatus(todoDto.getStatus());

            diff.put("writer", writer);
            diff.put("before", todoTmp.getStatus());
            diff.put("after", todoDto.getStatus());
            diff.put("message", writer + "님께서 상태를 " + todoTmp.getStatus() + "에서 " + todoDto.getStatus() + "(으)로 변경했습니다.");

            todoTmp.changeModifyDate();

            TodoRecord todoRecord = TodoRecord.builder()
                    .id(todoRecordId)
                    .diff(diff)
                    .todo_id(todoTmp.getId())
                    .modifyDate(LocalDateTime.now())
                    .build();

            todoRecordDao.save(todoRecord);
            todoDao.save(todoTmp);
            alarmService.addAlarm(todoDto.getTitle() + " : " + diff.get("message"), todoDto.getId());

        } catch (Exception e){
            e.printStackTrace();

            return false;
        }
        return true;
    }

    @Transactional
    public boolean moveTodoTeam(TodoDto todoDto) {

        try {

            String todoId = todoDto.getId();
            Todo todo = todoDao.findTodoById(todoId).orElseThrow(() -> new CustomException(ErrorCode.TODO_NOT_FOUND));
            Todo todoTmp = Todo.builder()
                    .id(todo.getId())
                    .title(todo.getTitle())
                    .status(todo.getStatus())
                    .projectId(todo.getProjectId())
                    .teamId(todo.getTeamId())
                    .memberId(todo.getMemberId())
                    .modifyDate(todo.getModifyDate())
                    .regDate(todo.getRegDate())
                    .build();

            IdGenerator idGenerator = new IdGenerator();
            String todoRecordId = idGenerator.generateId();
            while(todoRecordDao.existsById(todoRecordId)){
                todoRecordId = idGenerator.generateId();
            }

            // todo변경 시 diff에 저장
            Map<String, String> diff = new HashMap<>();

            Member member = memberDao.findMemberById(todoTmp.getMemberId()).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
            String writer = member.getName();

            todoTmp.changeBelong(todoDto.getTeamId(), todoDto.getMemberId());

            diff.put("writer", writer);
            diff.put("before", todoTmp.getTeamId());
            diff.put("after", todoDto.getTeamId());

            String changeStr = writer + "님께서 해당 할일의 팀을 " + todoTmp.getTeamId() + "에서 " + todoDto.getTeamId() + "(으)로 변경했습니다.";

            if (todoDto.getMemberId() != null) {
                // 할일이 다음 팀으로 보내지고 담당자도 정해졌을 때

                Member nextMember = memberDao.findMemberById(todoDto.getMemberId()).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
                String nextWriter = nextMember.getName();

                diff.put("before", writer);
                diff.put("after", nextWriter);
                changeStr += "\n할일의 담당자가 " + writer + "님에서 " + nextWriter + "님으로 변경되었습니다.";
            }

            diff.put("message", changeStr);

            todoTmp.changeModifyDate();

            TodoRecord todoRecord = TodoRecord.builder()
                    .id(todoRecordId)
                    .diff(diff)
                    .todo_id(todoTmp.getId())
                    .modifyDate(LocalDateTime.now())
                    .build();

            todoRecordDao.save(todoRecord);
            todoDao.save(todoTmp);
            alarmService.addAlarm(todoDto.getTitle() + " : " + diff.get("message"), todoDto.getId());

        } catch (Exception e){
            e.printStackTrace();

            return false;
        }
        return true;
    }

    @Transactional
    public boolean moveTodoMember(TodoDto todoDto) {

        try {

            String todoId = todoDto.getId();
            Todo todo = todoDao.findTodoById(todoId).orElseThrow(() -> new CustomException(ErrorCode.TODO_NOT_FOUND));
            Todo todoTmp = Todo.builder()
                    .id(todo.getId())
                    .title(todo.getTitle())
                    .status(todo.getStatus())
                    .projectId(todo.getProjectId())
                    .teamId(todo.getTeamId())
                    .memberId(todo.getMemberId())
                    .modifyDate(todo.getModifyDate())
                    .regDate(todo.getRegDate())
                    .build();

            IdGenerator idGenerator = new IdGenerator();
            String todoRecordId = idGenerator.generateId();
            while(todoRecordDao.existsById(todoRecordId)){
                todoRecordId = idGenerator.generateId();
            }

            // todo변경 시 diff에 저장
            Map<String, String> diff = new HashMap<>();

            String writer = "";
            if(todoTmp.getMemberId() != null) {
                Member member = memberDao.findMemberById(todoTmp.getMemberId()).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
                writer = member.getName();
            }

            Member nextMember = memberDao.findMemberById(todoDto.getMemberId()).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
            String nextWriter = nextMember.getName();

            if(writer.equals("")){
                diff.put("before", writer);
                diff.put("after", nextWriter);
                diff.put("message", "할일의 담당자가 " + nextWriter + "님으로 변경되었습니다.");
            } else {
                diff.put("before", writer);
                diff.put("after", nextWriter);
                diff.put("message", "할일의 담당자가 " + writer + "님에서 " + nextWriter + "님으로 변경되었습니다.");
            }

            todoTmp.changeBelong(todoDto.getTeamId(), todoDto.getMemberId());
            todoTmp.changeModifyDate();

            TodoRecord todoRecord = TodoRecord.builder()
                    .id(todoRecordId)
                    .diff(diff)
                    .todo_id(todoTmp.getId())
                    .modifyDate(LocalDateTime.now())
                    .build();

            todoRecordDao.save(todoRecord);
            todoDao.save(todoTmp);

            if(!writer.equals("")) alarmService.addAlarm(todoDto.getTitle() + " : " + diff.get("message"), todoDto.getId());

        } catch (Exception e){
            e.printStackTrace();

            return false;
        }
        return true;
    }

}
