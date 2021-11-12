package com.web.tcp.todo;

import com.web.tcp.alarm.AlarmController;
import com.web.tcp.alarm.AlarmService;
import com.web.tcp.error.CustomException;
import com.web.tcp.error.ErrorCode;
import com.web.tcp.member.Member;
import com.web.tcp.member.MemberDao;
import com.web.tcp.team.Team;
import com.web.tcp.team.TeamDao;
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
    TeamDao teamDao;
    TodoDao todoDao;
    TodoRecordDao todoRecordDao;
    AlarmController alarmController;
    AlarmService alarmService;

    @Transactional
    public boolean addTodo(TodoDto todoDto) {

        try{

            IdGenerator idGenerator = new IdGenerator();
            String tid = idGenerator.generateId();
            while(todoDao.existsById(tid)){
                tid = idGenerator.generateId();
            }

            todoDto.setId(tid);
            todoDto.setRegDate(LocalDateTime.now());
            todoDto.setModifyDate(LocalDateTime.now());
            Todo todo = TodoAdaptor.dtoToEntity(todoDto);

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
            TodoDto todoDto = null;

            List<Todo> todoList = todoDao.findTodosByProjectId(projectId);
            for(Todo todo : todoList) {
                todoDto = TodoAdaptor.entityToDto(todo);

                Team team = teamDao.findTeamById(todo.getTeamId()).orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));
                todoDto.setTeamName(team.getName());

                todoDto.setMemberName("담당자 없음");
                if(Optional.ofNullable(todo.getMemberId()).isPresent()){
                    Member member = memberDao.findMemberById(todo.getMemberId()).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
                    todoDto.setMemberName(member.getName());
                }

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
            Todo todoTmp = TodoAdaptor.dupEntity(todo);

            IdGenerator idGenerator = new IdGenerator();
            String todoRecordId = idGenerator.generateId();
            while(todoRecordDao.existsById(todoRecordId)){
                todoRecordId = idGenerator.generateId();
            }

            // todo변경 시 diff에 저장
            Map<String, String> diff = new HashMap<>();

            Member member = memberDao.findMemberById(todoTmp.getMemberId()).orElse(null);
            String writer = member.getName();

            diff.put("writer", writer);
            diff.put("before", todoTmp.getStatus());
            diff.put("after", todoDto.getStatus());
            diff.put("message", writer + "님께서 상태를 " + todoTmp.getStatus() + "에서 " + todoDto.getStatus() + "(으)로 변경했습니다.");

            todoTmp.changeStatus(todoDto.getStatus());
            todoTmp.changeModifyDate();

            TodoRecord todoRecord = setTodoRecord(todoRecordId, diff, todoTmp.getId());

            todoRecordDao.save(todoRecord);
            todoDao.save(TodoAdaptor.dtoToEntity(todoDto));
            alarmController.spreadAlarm(todoDto.getTitle() + " : " + diff.get("message"), todoDto.getId());

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
            Todo todoTmp = TodoAdaptor.dupEntity(todo);

            IdGenerator idGenerator = new IdGenerator();
            String todoRecordId = idGenerator.generateId();
            while(todoRecordDao.existsById(todoRecordId)){
                todoRecordId = idGenerator.generateId();
            }

            // todo변경 시 diff에 저장
            Map<String, String> diff = new HashMap<>();
            System.out.println("todoTmp : " + todoTmp);
            Member member = memberDao.findMemberById(todoTmp.getMemberId()).orElse(null);
            String writer = "";
            if(member == null ){
                writer = "()";
            } else {
                writer = member.getName();
            }

            Team team = teamDao.findTeamById(todoTmp.getTeamId()).orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));
            String beforeTeam = team.getName();

            diff.put("writer", writer);
            diff.put("before", todoTmp.getTeamId());
            diff.put("after", todoDto.getTeamId());

            String changeStr = writer + "님께서 해당 할일의 팀을 " + beforeTeam + "에서 " + todoDto.getTeamName() + "(으)로 변경했습니다.";

            if (todoDto.getMemberId() != null) {
                // 할일이 다음 팀으로 보내지고 담당자도 정해졌을 때

                Member nextMember = memberDao.findMemberById(todoDto.getMemberId()).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
                String nextWriter = nextMember.getName();

                diff.put("before", writer);
                diff.put("after", nextWriter);
                changeStr += "\n할일의 담당자가 " + writer + "님에서 " + nextWriter + "님으로 변경되었습니다.";
            }

            diff.put("message", changeStr);

            todoTmp.changeBelong(todoDto.getTeamId(), todoDto.getMemberId());
            todoTmp.changeModifyDate();

            TodoRecord todoRecord = setTodoRecord(todoRecordId, diff, todoTmp.getId());

            todoRecordDao.save(todoRecord);
            todoDao.save(TodoAdaptor.dtoToEntity(todoDto));
            alarmController.spreadAlarm(todoDto.getTitle() + " : " + diff.get("message"), todoDto.getId());

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
            Todo todoTmp = TodoAdaptor.dupEntity(todo);

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

            Member nextMember = memberDao.findMemberById(todoTmp.getMemberId()).orElse(null);
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

            TodoRecord todoRecord = setTodoRecord(todoRecordId, diff, todoTmp.getId());

            todoRecordDao.save(todoRecord);
            todoDao.save(TodoAdaptor.dtoToEntity(todoDto));

            if(!writer.equals("")) alarmController.spreadAlarm(todoDto.getTitle() + " : " + diff.get("message"), todoDto.getId());

        } catch (Exception e){
            e.printStackTrace();

            return false;
        }
        return true;
    }

    public TodoDto getTodoInfo(String todoId) {

        Todo todo = todoDao.findTodoById(todoId).orElseThrow(() -> new CustomException(ErrorCode.TODO_NOT_FOUND));
        TodoDto todoDto = TodoAdaptor.entityToDto(todo);
        todoDto.setMemberName("담당자 없음");
        if(Optional.ofNullable(todo.getMemberId()).isPresent()) {
            Member member = memberDao.findMemberById(todoDto.getMemberId()).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
            todoDto.setMemberName(member.getName());
        }
        Team team = teamDao.findTeamById(todoDto.getTeamId()).orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        todoDto.setTeamName(team.getName());

        return todoDto;
    }

    private TodoRecord setTodoRecord(String todoRecordId, Map<String, String> diff, String todoId){
        return TodoRecord.builder()
                .id(todoRecordId)
                .diff(diff)
                .todo_id(todoId)
                .modifyDate(LocalDateTime.now())
                .build();
    }
}
