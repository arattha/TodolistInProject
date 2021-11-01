package com.web.tip.todo.content;

import com.web.tip.BasicResponse;
import com.web.tip.todo.content.request.ContentModifyRequest;
import com.web.tip.todo.content.request.ContentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/todo_content")
public class TodoContentController {
    private final TodoContentService todoContentService;

    private static final String SUCCESS = "success";

    @GetMapping()
    public ResponseEntity<Object> getTodoContents(@RequestParam String todoId) {
        List<TodoContentDto> todoContentDtos = todoContentService.getTodoContents(todoId);

        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;
        result.object = todoContentDtos;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PostMapping()
    public ResponseEntity<Object> addTodoContents(@RequestBody ContentRequest request) {
        TodoContentDto todoContentDto = todoContentService.addTodoContent(request);

        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;
        result.object = todoContentDto;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<Object> modifyTodoContents(@RequestBody ContentModifyRequest request) {
        TodoContentDto todoContentDto = todoContentService.modifyTodoContent(request);

        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;
        result.object = todoContentDto;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<Object> deleteTodoContent(@RequestParam String todoContentId) {
        TodoContentDto todoContentDto = todoContentService.deleteTodoContent(todoContentId);

        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;
        result.object = todoContentDto;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
