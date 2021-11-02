package com.web.tip.todo.content;

import com.web.tip.BasicResponse;
import com.web.tip.todo.TodoRecordDto;
import com.web.tip.todo.TodoRecordService;
import com.web.tip.todo.content.record.TodoContentRecordService;
import com.web.tip.todo.content.request.ContentModifyRequest;
import com.web.tip.todo.content.request.ContentRequest;
import com.web.tip.todo.content.url.TodoUrlDto;
import com.web.tip.todo.content.url.TodoUrlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/todo_content")
public class TodoContentController {
    private final TodoContentService todoContentService;
    private final TodoUrlService todoUrlService;
    private final TodoRecordService todoRecordService;
    private final TodoContentRecordService todoContentRecordService;

    private static final String SUCCESS = "success";

    @GetMapping()
    public ResponseEntity<Object> getTodoContents(@RequestParam(value = "id") String todoId) {
        log.info("todo:{} 에서 content 요청", todoId);
        List<TodoContentDto> todoContentDtos = todoContentService.getTodoContents(todoId);
        log.info("todo_content {}개 발견", todoContentDtos.size());

        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;
        result.object = todoContentDtos;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PostMapping()
    public ResponseEntity<Object> addTodoContents(@RequestBody ContentRequest request) {
        log.info("member:{} 가 todo:{} 에 content 추가 요청", request.getMemberId(), request.getTodoId());
        TodoContentDto todoContentDto = todoContentService.addTodoContent(request);
        log.info("content:{} 가 추가 완료", todoContentDto.getId());

        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<Object> modifyTodoContents(@RequestBody ContentModifyRequest request) {
        log.info("content:{} 내용 수정 요청", request.getId());
        TodoContentDto todoContentDto = todoContentService.modifyTodoContent(request);
        log.info("content:{} 내용 수정 완료", todoContentDto.getId());

        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<Object> deleteTodoContent(@RequestParam(value = "id") String todoContentId) {
        log.info("content:{} 삭제 요청", todoContentId);
        TodoContentDto todoContentDto = todoContentService.deleteTodoContent(todoContentId);
        log.info("content:{} 삭제 완료", todoContentDto.getId());

        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/url")
    public ResponseEntity<Object> getTodoUrls(@RequestParam(value = "id") String todoId) {
        log.info("todo:{} 에서 todoUrl 요청", todoId);
        List<TodoUrlDto> todoUrls = todoUrlService.findUrlsByTodoContent(todoId);
        log.info("todo_url {}개 발견", todoUrls.size());

        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;
        result.object = todoUrls;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/record")
    public ResponseEntity<Object> getTodoRecords(@RequestParam(value = "id") String todoId) {
        log.info("todo:{} 에서 todoRecord 요청", todoId);
        List<TodoRecordDto> todoRecordDtos = todoRecordService.findTodoRecordsByTodoId(todoId);
        log.info("todoRecord {}개 발견", todoRecordDtos.size());
        List<TodoRecordDto> todoContentRecordDtos = todoContentRecordService.findTodoContentRecords(todoId);
        log.info("todoContentRecord {}개 발견", todoContentRecordDtos.size());

        List<TodoRecordDto> records = new ArrayList<>();
        if(!todoRecordDtos.isEmpty()){
            records.addAll(todoRecordDtos);
        }
        if(!todoContentRecordDtos.isEmpty()){
            records.addAll(todoContentRecordDtos);
        }
        Collections.sort(records, (o1, o2) -> o2.getModifyDate().compareTo(o1.getModifyDate()));

        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;
        result.object = records;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
