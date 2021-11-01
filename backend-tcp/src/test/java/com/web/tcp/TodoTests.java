package com.web.tcp;

import com.web.tcp.error.CustomException;
import com.web.tcp.error.ErrorCode;
import com.web.tcp.todo.Todo;
import com.web.tcp.todo.TodoDto;
import com.web.tcp.todo.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@Slf4j
@SpringBootTest
public class TodoTests {

    @Autowired
    TodoService todoService;

    @Test
    void createTodo(){
        Assertions.assertTrue(todoService.addTodo(TodoDto.builder()
                .title("test2")
                .status("접수")
                .projectId("1231231231231")
                .teamId("1231231231231")
                .memberId("1231231231231")
                .modifyDate(LocalDateTime.now())
                .regDate(LocalDateTime.now())
                .build()
        ), "Todo 추가 실패");
    }

    @Test
    void getTodoList(){
        Assertions.assertNotNull(todoService.getTodoList("1231231231231"), "Todo 목록 반환 실패");
    }

    @Test
    void moveTodo(){
        Todo todo = todoService.getTodo("test");
        log.info(todo.toString());
        Assertions.assertTrue(todoService.moveTodo(TodoDto.builder()
                .id(todo.getId())
                .title("test")
                .status("접수")
                .projectId("1231231231231")
                .teamId("2342342342342")
                .memberId("1231231231231")
                .modifyDate(LocalDateTime.now())
                .regDate(LocalDateTime.now())
                .build()
        ), "Todo 이동 실패");
    }

}
