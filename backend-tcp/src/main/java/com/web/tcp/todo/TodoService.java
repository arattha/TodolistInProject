package com.web.tcp.todo;

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
            diff.put("diff", todo.getTitle() + "이(가) 생성되었습니다.");
            TodoRecord todoRecord = TodoRecord.builder()
                    .id(todoRecordId)
                    .diff(diff)
                    .todo_id(todo.getId())
                    .modify_date(LocalDateTime.now())
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

                todoDto = todoDto.builder()
                        .id(todo.getId())
                        .title(todo.getTitle())
                        .status(todo.getStatus())
                        .projectId(todo.getProjectId())
                        .teamId(todo.getTeamId())
                        .memberId(todo.getMemberId())
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

    @Transactional
    public boolean moveTodo(TodoDto todoDto) {
        // 해당 Todo의 변경된 데이터를 DB에 반영하고
        // 이를 todoRecord DB에 저장

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
            String beforeWriter = member.getName();

            // status
            if (checkStatus(todoTmp.getStatus(), todoDto.getStatus())) {

                todo.changeStatus(todoDto.getStatus());

                diff.put("writer", beforeWriter);
                diff.put("beforeStatus", todoTmp.getStatus());
                diff.put("afterStatus", todoDto.getStatus());
                diff.put("diff", beforeWriter + "님께서 상태를 " + todoTmp.getStatus() + "에서 " + todoDto.getStatus() + "(으)로 변경했습니다.");

            }
            // team
            else if (checkTeam(todoTmp.getTeamId(), todoDto.getTeamId())) {

                todoTmp.changeBelong(todoDto.getTeamId(), todoDto.getMemberId());

                diff.put("writer", beforeWriter);
                diff.put("beforeTeamId", todoTmp.getTeamId());
                diff.put("afterTeamId", todoDto.getTeamId());

                String changeStr = beforeWriter + "님께서 해당 할일의 팀을 " + todoTmp.getTeamId() + "에서 " + todoDto.getTeamId() + "(으)로 변경했습니다.";

                if (todoDto.getMemberId() != null) {
                    // 할일이 다음 팀으로 보내지고 담당자도 정해졌을 때

                    Member nextMember = memberDao.findMemberById(todoDto.getMemberId()).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
                    String nextWriter = nextMember.getName();

                    diff.put("beforeMember", beforeWriter);
                    diff.put("afterMember", nextWriter);
                    changeStr += "\n할일의 담당자가 " + beforeWriter + "님에서 " + nextWriter + "님으로 변경되었습니다.";
                }

                diff.put("diff", changeStr);
            }
            // 담당자만 변경되었을 때
            else if (checkMember(todoTmp.getMemberId(), todoDto.getMemberId())) {

                todoTmp.changeBelong(todoDto.getTeamId(), todoDto.getMemberId());

                Member nextMember = memberDao.findMemberById(todoDto.getMemberId()).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
                String nextWriter = nextMember.getName();

                diff.put("beforeMember", beforeWriter);
                diff.put("afterMember", nextWriter);
                diff.put("diff", "할일의 담당자가 " + beforeWriter + "님에서 " + nextWriter + "님으로 변경되었습니다.");
            }

            if(!diff.isEmpty()) {


                TodoRecord todoRecord = TodoRecord.builder()
                        .id(todoRecordId)
                        .diff(diff)
                        .todo_id(todoTmp.getId())
                        .modify_date(LocalDateTime.now())
                        .build();

                todoRecordDao.save(todoRecord);
            }

            todoTmp.changeModifyDate();
            todoDao.save(todoTmp);

        } catch (Exception e){
            e.printStackTrace();

            return false;
        }

        return true;

    }

    private boolean checkStatus(String beforeStatus, String afterStatus){
        if(beforeStatus.equals(afterStatus)) return false;
        return true;
    }

    private boolean checkTeam(String beforeTeam, String afterTeam){
        if(beforeTeam.equals(afterTeam)) return false;
        return true;
    }

    private boolean checkMember(String beforeMember, String afterMember){
        if(beforeMember.equals(afterMember)) return false;
        return true;
    }

}
