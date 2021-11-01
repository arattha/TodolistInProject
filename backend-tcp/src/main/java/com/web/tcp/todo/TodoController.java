package com.web.tcp.todo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
@RequestMapping("/todo")
public class TodoController {

    private final SimpMessagingTemplate template;

    TodoService todoService;

    // client가 '/server/addTodo'경로로 새롭게 추가할 Todo에 관한 데이터를 전송
    // Todo를 포함하고 있는 project를 구독 중인 client들에게 send
    @MessageMapping(value = "/addTodo")
    public void addTodo(TodoDto todoDto){
        String projectId = todoDto.getProject_id();
        todoService.addTodo(todoDto);
        template.convertAndSend("/client/todo/" + projectId, todoService.getTodoList(projectId));
    }

    // client가 '/server/getTodo'경로로 프로젝트 아이디 전송
    // 해당 프로젝트를 구독 중인 client들에게 send
    @MessageMapping(value = "/getTodo")
    public void getTodo(String projectId){
        template.convertAndSend("/client/todo/" + projectId, todoService.getTodoList(projectId));
    }

    // client가 '/server/moveTodo'경로로 이동한 TodoDto 전송
    // 수정된 Todo의 목록들을 해당 프로젝트를 구독 중인 client들에게 전송
    @MessageMapping(value = "/moveTodo")
    public void moveTodo(TodoDto todoDto){
        String projectId = todoDto.getProject_id();
        todoService.moveTodo(todoDto);
        template.convertAndSend("/client/todo/" + projectId, todoService.getTodoList(projectId));
    }

}
