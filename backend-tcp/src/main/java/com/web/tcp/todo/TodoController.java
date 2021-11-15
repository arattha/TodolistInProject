package com.web.tcp.todo;

import com.web.tcp.alarm.AlarmService;
import com.web.tcp.error.CustomException;
import com.web.tcp.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class TodoController {

    private final SimpMessagingTemplate template;

    AlarmService alarmService;
    TodoService todoService;
    TodoDao todoDao;

    // client가 '/server/addTodo'경로로 새롭게 추가할 Todo에 관한 데이터를 전송
    // Todo를 포함하고 있는 project를 구독 중인 client들에게 send
    @MessageMapping(value = "/addTodo")
    public void addTodo(TodoDto todoDto){
        String projectId = todoDto.getProjectId();
        todoService.addTodo(todoDto);
        template.convertAndSend("/client/todo/" + projectId, todoService.getTodoList(projectId));
        template.convertAndSend("/client/todo/" + todoDto.getProjectId() + "/" + todoDto.getMemberId(),
                todoService.getTodoMyList(todoDto.getProjectId(), todoDto.getMemberId()));
    }

    // client가 '/server/getTodo'경로로 프로젝트 아이디 전송
    // 해당 프로젝트를 구독 중인 client들에게 send
    @MessageMapping(value = "/getTodo")
    public void getTodo(String projectId){

        try{
            projectId = (String) StringToJson(projectId).get("projectId");
            template.convertAndSend("/client/todo/" + projectId, todoService.getTodoList(projectId));
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    // client가 '/server/getTodo'경로로 프로젝트 아이디 전송
    // 해당 프로젝트를 구독 중인 client들에게 send
    @MessageMapping(value = "/getMyTodo")
    public void getMyTodo(String projectId, String memberId){

        try{
            projectId = (String) StringToJson(projectId).get("projectId");
            memberId = (String) StringToJson(memberId).get("memberId");
            template.convertAndSend("/client/todo/" + projectId + "/" + memberId, todoService.getTodoMyList(projectId, memberId));
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    // client가 '/server/getTodo'경로로 프로젝트 아이디 전송
    // 해당 프로젝트를 구독 중인 client들에게 send
    @MessageMapping(value = "/getTeamTodo")
    public void getTeamTodo(String projectId, String teamId){

        try{
            projectId = (String) StringToJson(projectId).get("projectId");
            teamId = (String) StringToJson(teamId).get("teamId");
            template.convertAndSend("/client/todo/" + projectId + "/team/" + teamId, todoService.getTodoTeamList(projectId, teamId));
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @MessageMapping(value = "/updateTodo")
    public void updateTodo(TodoDto todoDto){
        log.info(todoDto.toString());
        TodoDto updatedTodo = todoService.updateTodo(todoDto);

        String projectId = updatedTodo.getProjectId();
        template.convertAndSend("/client/todo/" + projectId, todoService.getTodoList(projectId));
        template.convertAndSend("/client/detail/" + todoDto.getId(), todoService.getTodoInfo(todoDto.getId()));
    }

    // client가 '/server/moveTodo'경로로 이동한 TodoDto 전송
    // 수정된 Todo의 목록들을 해당 프로젝트를 구독 중인 client들에게 전송
    @MessageMapping(value = "/moveTodo/{type}")
    public void moveTodo(TodoDto todoDto, @DestinationVariable("type") String type){

        Todo tmp = todoDao.findTodoById(todoDto.getId()).orElseThrow(() -> new CustomException(ErrorCode.TODO_NOT_FOUND));
        String beforeMid = tmp.getMemberId();

        if(type.equals("status")){
            todoService.moveTodoStatus(todoDto);
        } else if(type.equals("team")) {
            todoService.moveTodoTeam(todoDto);
        } else if(type.equals("member")){
            todoService.moveTodoMember(todoDto);
        } else {
            return;
        }

        String projectId = todoDto.getProjectId();
        String memberId = todoDto.getMemberId();
        String teamId = todoDto.getTeamId();
        template.convertAndSend("/client/todo/" + projectId, todoService.getTodoList(projectId));
        template.convertAndSend("/client/detail/" + todoDto.getId(), todoService.getTodoInfo(todoDto.getId()));
        template.convertAndSend("/client/todo/" + projectId + "/team/" + teamId, todoService.getTodoTeamList(projectId, teamId));
        if(beforeMid != null)
            template.convertAndSend("/client/todo/" + projectId + "/" + beforeMid, todoService.getTodoMyList(projectId, beforeMid));
        template.convertAndSend("/client/todo/" + projectId + "/" + memberId, todoService.getTodoMyList(projectId, memberId));
    }

    // client가 '/server/getTodoInfo'경로로 Member의 Id와 Todo의 Id 전송
    // Todo정보를 해당 client에게 send
    @MessageMapping(value = "/getTodoInfo")
    public void getTodoInfo(String todoId){
        try{
            todoId = (String) StringToJson(todoId).get("todoId");
            template.convertAndSend("/client/detail/" + todoId, todoService.getTodoInfo(todoId));
        } catch (Exception e){
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
