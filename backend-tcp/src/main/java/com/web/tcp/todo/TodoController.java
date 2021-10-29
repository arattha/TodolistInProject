package com.web.tcp.todo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
@RequestMapping("/todo")
public class TodoController {

    TodoService todoService;

    // /receive를 메시지를 받을 endpoint로 설정합니다.
    @MessageMapping("/addTodo")

    // /send로 메시지를 반환합니다.
    @SendTo("/sendTodo")

    // SocketHandler는 1) /receive에서 메시지를 받고, /send로 메시지를 보내줍니다.
    // 정의한 SocketVO를 1) 인자값, 2) 반환값으로 사용합니다.
    public List<Todo> addTodo(TodoDto todoDto) {

        log.info("새로운 Todo 추가" + todoDto.toString());

        return todoService.addTodo(todoDto);

    }
}
