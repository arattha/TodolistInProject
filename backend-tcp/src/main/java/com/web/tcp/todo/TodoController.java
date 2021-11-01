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
        template.convertAndSend("/client/todo/" + todoDto.getProject_id(), todoService.addTodo(todoDto));
    }

    // client가 '/server/getTodo'경로로 새롭게 추가할 Todo에 관한 데이터를 전송
    // Todo를 포함하고 있는 project를 구독 중인 client들에게 send
    @MessageMapping(value = "/getTodo")
    public void getTodo(String projectId){
        template.convertAndSend("/client/todo/" + projectId, todoService.getTodo(projectId));
    }

}
