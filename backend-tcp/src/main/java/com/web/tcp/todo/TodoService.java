package com.web.tcp.todo;

import com.web.tcp.alarm.AlarmController;
import com.web.tcp.alarm.AlarmService;
import com.web.tcp.common.MemberHasTeamDao;
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
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class TodoService {

    MemberDao memberDao;
    MemberHasTeamDao memberHasTeamDao;
    TeamDao teamDao;
    TodoDao todoDao;
    TodoRecordDao todoRecordDao;
    AlarmController alarmController;
    AlarmService alarmService;

    @Transactional
    public boolean addTodo(TodoDto todoDto) {
        IdGenerator idGenerator = new IdGenerator();
        String tid = idGenerator.generateId();
        while (todoDao.existsById(tid)) {
            tid = idGenerator.generateId();
        }

        todoDto.setId(tid);
        todoDto.setRegDate(LocalDateTime.now());
        todoDto.setModifyDate(LocalDateTime.now());
        Todo todo = TodoAdaptor.dtoToEntity(todoDto);

        try {
            createTodoRecord(todoDao.save(todo));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Transactional
    public void addTodoContent(TodoContentDto todoContentDto){
        Todo todo = todoDao.findTodoById(todoContentDto.getTodoId())
                .orElseThrow(() -> new CustomException(ErrorCode.TODO_NOT_FOUND));
        Member member = memberDao.findMemberById(todoContentDto.getMemberId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        String content = todo.getTitle() + ": " + member.getName() + "????????? " + todo.getTitle() +"??? ?????? ????????? ??????????????????.";
       alarmController.spreadAlarm(content, todoContentDto.getTodoId());
    }

    private void createTodoRecord(Todo todo) throws JpaSystemException {
        IdGenerator idGenerator = new IdGenerator();
        String todoRecordId = idGenerator.generateId();
        while (todoRecordDao.existsById(todoRecordId)) {
            todoRecordId = idGenerator.generateId();
        }

        Map<String, String> diff = new HashMap<>();
        diff.put("message", todo.getTitle() + "???(???) ?????????????????????.");
        TodoRecord todoRecord = TodoRecord.builder()
                .id(todoRecordId)
                .diff(diff)
                .todo_id(todo.getId())
                .modifyDate(LocalDateTime.now())
                .build();

        todoRecordDao.save(todoRecord);
    }

    @Transactional
    public Object getTodoList(String projectId) {

        List<TodoDto> todoDtoList = new ArrayList<>();
        try {
            TodoDto todoDto = null;

            List<Todo> todoList = todoDao.findTodosByProjectId(projectId);
            for (Todo todo : todoList) {
                todoDto = TodoAdaptor.entityToDto(todo);

                Team team = teamDao.findTeamById(todo.getTeamId()).orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));
                todoDto.setTeamName(team.getName());

                todoDto.setMemberName("????????? ??????");
                if (Optional.ofNullable(todo.getMemberId()).isPresent()) {
                    Member member = memberDao.findMemberById(todo.getMemberId()).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
                    todoDto.setMemberName(member.getName());
                }

                todoDtoList.add(todoDto);
            }

            return refactoringTotalTodo(todoDtoList, projectId);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private Object refactoringTotalTodo(List<TodoDto> todoDtoList, String projectId) {

        try {

            List<Team> teamList = teamDao.findTeamByProjectIdAndIsUse(projectId, true);
            int size = teamList.size();

            List<Object> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                Map<String, Object> teamInfo = new HashMap<>();
                teamInfo.put("teamId", teamList.get(i).getId());
                teamInfo.put("teamName", teamList.get(i).getName());

                List<TodoDto> tmp = new ArrayList<>();
                for (TodoDto todoDto : todoDtoList) {
                    if (todoDto.getTeamId().equals(teamList.get(i).getId())) {
                        tmp.add(todoDto);
                    }
                }
                teamInfo.put("todoInfoList", tmp);

                list.add(teamInfo);
            }

            return list;

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }

    }

    @Transactional
    public Object getTodoMyList(String projectId, String memberId) {
        List<TodoDto> todoDtoList = new ArrayList<>();
        try{
            TodoDto todoDto = null;

            List<Todo> todoList = todoDao.findTodosByProjectIdAndMemberId(projectId, memberId);
            for(Todo todo : todoList) {
                todoDto = TodoAdaptor.entityToDto(todo);

                Team team = teamDao.findTeamById(todo.getTeamId()).orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));
                todoDto.setTeamName(team.getName());
                Member member = memberDao.findMemberById(todo.getMemberId()).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
                todoDto.setMemberName(member.getName());

                todoDtoList.add(todoDto);
            }

            return refactoringTodo(todoDtoList);

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public Object getTodoTeamList(String projectId, String teamId) {
        List<TodoDto> todoDtoList = new ArrayList<>();
        try{
            TodoDto todoDto = null;

            List<Todo> todoList = todoDao.findTodosByProjectIdAndTeamId(projectId, teamId);
            for(Todo todo : todoList) {
                todoDto = TodoAdaptor.entityToDto(todo);

                Team team = teamDao.findTeamById(todo.getTeamId()).orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));
                todoDto.setTeamName(team.getName());
                Member member = memberDao.findMemberById(todo.getMemberId()).orElse(null);
                if(member != null) todoDto.setMemberName(member.getName());
                else todoDto.setMemberName("????????? ??????");

                todoDtoList.add(todoDto);
            }

            return refactoringTodo(todoDtoList);

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private Object refactoringTodo(List<TodoDto> todoDtoList) {

        try {

            String statusList[] = new String[] {"New", "??????", "??????", "??????", "??????????????????"};
            List<Object> list = new ArrayList<>();

            for(String status : statusList){
                Map<String, Object> teamInfo = new HashMap<>();
                teamInfo.put("status", status);

                List<TodoDto> tmp = new ArrayList<>();
                for(TodoDto todoDto : todoDtoList){
                    if(todoDto.getStatus().equals(status)){
                        tmp.add(todoDto);
                    }
                }
                teamInfo.put("todoList", tmp);

                list.add(teamInfo);
            }

            return list;

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }

    }

    @Transactional
    public TodoDto updateTodo(TodoDto todoDto){
        Todo todo = todoDao.findTodoById(todoDto.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.TODO_NOT_FOUND));

        TodoDto updated = TodoAdaptor.entityToDto(todo);
        updated.setTitle(todoDto.getTitle());
        updated.setModifyDate(LocalDateTime.now());

        return TodoAdaptor.entityToDto(todoDao.save(TodoAdaptor.dtoToEntity(updated)));
    }
    
    @Transactional
    public Todo getTodo(String todoTitle) {

        Todo todo = todoDao.findTodoByTitle(todoTitle).orElseThrow(() -> new CustomException(ErrorCode.TODO_NOT_FOUND));
        return todo;

    }

    // moveTodoStatus, moveTodoTeam, moveTodoMember
    // ?????? Todo??? ????????? ???????????? DB??? ????????????
    // ?????? todoRecord DB??? ??????
    @Transactional
    public boolean moveTodoStatus(TodoDto todoDto) {

        try {

            String todoId = todoDto.getId();
            Todo todo = todoDao.findTodoById(todoId).orElseThrow(() -> new CustomException(ErrorCode.TODO_NOT_FOUND));
            Todo todoTmp = TodoAdaptor.dupEntity(todo);

            IdGenerator idGenerator = new IdGenerator();
            String todoRecordId = idGenerator.generateId();
            while (todoRecordDao.existsById(todoRecordId)) {
                todoRecordId = idGenerator.generateId();
            }

            // todo?????? ??? diff??? ??????
            Map<String, String> diff = new HashMap<>();

            Member member = memberDao.findMemberById(todoDto.getMemberId()).orElse(null);
            String writer = "";
            if(member != null){
                writer = member.getName();
            }

            diff.put("writer", writer);
            diff.put("before", todoTmp.getStatus());
            diff.put("after", todoDto.getStatus());
            if(member == null){
                diff.put("message", "????????? " + todoTmp.getStatus() + "?????? " + todoDto.getStatus() + "(???)??? ??????????????????.");
            } else {
                diff.put("message", writer + "????????? ????????? " + todoTmp.getStatus() + "?????? " + todoDto.getStatus() + "(???)??? ??????????????????.");
            }

            todoTmp.changeStatus(todoDto.getStatus());
            todoTmp.changeModifyDate();

            TodoRecord todoRecord = setTodoRecord(todoRecordId, diff, todoTmp.getId());

            todoRecordDao.save(todoRecord);

            if(todoDto.getStatus().equals("New")){
                // ??? ?????? ????????? New??? ??????????????? ???????????? ???????????????.
                todoDto.setMemberId(null);
            }
            todoDao.save(TodoAdaptor.dtoToEntity(todoDto));
            alarmController.spreadAlarm(todoDto.getTitle() + " : " + diff.get("message"), todoDto.getId());

        } catch (Exception e) {
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
            while (todoRecordDao.existsById(todoRecordId)) {
                todoRecordId = idGenerator.generateId();
            }

            // todo?????? ??? diff??? ??????
            Map<String, String> diff = new HashMap<>();

            Member member = memberDao.findMemberById(todoTmp.getMemberId()).orElse(null);
            String writer = "";
            if (member != null) {
                writer = member.getName();
            }

            Team team = teamDao.findTeamById(todoTmp.getTeamId()).orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));
            String beforeTeam = team.getName();

            diff.put("writer", writer);
            diff.put("before", todoTmp.getTeamId());
            diff.put("after", todoDto.getTeamId());

            String changeStr = "";
            if(member != null)
                changeStr = writer + "????????? ?????? ????????? ?????? " + beforeTeam + "?????? " + todoDto.getTeamName() + "(???)??? ??????????????????.";
            else
                changeStr = "?????? ????????? ?????? " + beforeTeam + "?????? " + todoDto.getTeamName() + "(???)??? ?????????????????????.";

            if (todoDto.getMemberId() != null) {
                // ????????? ?????? ????????? ???????????? ???????????? ???????????? ???

                Member nextMember = memberDao.findMemberById(todoDto.getMemberId()).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
                String nextWriter = nextMember.getName();

                diff.put("before", writer);
                diff.put("after", nextWriter);

                if(member == null)
                    changeStr += "\n????????? ???????????? " + nextWriter + "????????? ?????????????????????.";
                else
                    changeStr += "\n????????? ???????????? " + writer + "????????? " + nextWriter + "????????? ?????????????????????.";
            }

            diff.put("message", changeStr);

            todoTmp.changeBelong(todoDto.getTeamId(), todoDto.getMemberId());
            todoTmp.changeModifyDate();

            TodoRecord todoRecord = setTodoRecord(todoRecordId, diff, todoTmp.getId());

            todoRecordDao.save(todoRecord);
            todoDao.save(TodoAdaptor.dtoToEntity(todoDto));
            alarmController.spreadAlarm(todoDto.getTitle() + " : " + diff.get("message"), todoDto.getId());

        } catch (Exception e) {
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
            while (todoRecordDao.existsById(todoRecordId)) {
                todoRecordId = idGenerator.generateId();
            }

            // todo?????? ??? diff??? ??????
            Map<String, String> diff = new HashMap<>();

            String writer = "";

//            Member member = memberDao.findMemberById(todoTmp.getMemberId()).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
            Member member = memberDao.findMemberById(todoTmp.getMemberId()).orElse(null);
            if(member != null)
                writer = member.getName();

            Member nextMember = memberDao.findMemberById(todoDto.getMemberId()).orElse(null);
            String nextWriter = "";
            if(nextMember != null){
                nextWriter = nextMember.getName();
            }

            if(writer.equals(nextWriter)) return true;

            diff.put("before", writer);
            diff.put("after", nextWriter);

            if (writer.equals("")) {
                if(!nextWriter.equals("")) diff.put("message", "????????? ???????????? " + nextWriter + "????????? ?????????????????????.");
                else diff.put("message", "????????? ???????????? ???????????? ?????? ???????????? ???????????????.");
            }
            else {
                if(!nextWriter.equals("")) diff.put("message", "????????? ???????????? " + writer + "????????? " + nextWriter + "????????? ?????????????????????.");
                else diff.put("message", "????????? ???????????? ???????????? ?????? ???????????? ???????????????.");
            }

            todoTmp.changeBelong(todoDto.getTeamId(), todoDto.getMemberId());
            todoTmp.changeModifyDate();

            TodoRecord todoRecord = setTodoRecord(todoRecordId, diff, todoTmp.getId());

            todoRecordDao.save(todoRecord);
            todoDao.save(TodoAdaptor.dtoToEntity(todoDto));

            if (!writer.equals(""))
                alarmController.spreadAlarm(todoDto.getTitle() + " : " + diff.get("message"), todoDto.getId());

        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
        return true;
    }

    public TodoDto getTodoInfo(String todoId) {

        Todo todo = todoDao.findTodoById(todoId).orElseThrow(() -> new CustomException(ErrorCode.TODO_NOT_FOUND));
        TodoDto todoDto = TodoAdaptor.entityToDto(todo);
        todoDto.setMemberName("????????? ??????");
        if (Optional.ofNullable(todo.getMemberId()).isPresent()) {
            Member member = memberDao.findMemberById(todoDto.getMemberId()).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
            todoDto.setMemberName(member.getName());
        }
        Team team = teamDao.findTeamById(todoDto.getTeamId()).orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));

        todoDto.setTeamName(team.getName());

        return todoDto;
    }

    private TodoRecord setTodoRecord(String todoRecordId, Map<String, String> diff, String todoId) {
        return TodoRecord.builder()
                .id(todoRecordId)
                .diff(diff)
                .todo_id(todoId)
                .modifyDate(LocalDateTime.now())
                .build();
    }

}
