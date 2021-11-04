package com.web.tip.todo.content.record;

import com.web.tip.error.CustomException;
import com.web.tip.error.ErrorCode;
import com.web.tip.todo.Todo;
import com.web.tip.todo.TodoDao;
import com.web.tip.todo.TodoRecordDto;
import com.web.tip.todo.content.TodoContent;
import com.web.tip.todo.content.TodoContentDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TodoContentRecordService {
    private final TodoContentDao todoContentDao;
    private final TodoDao todoDao;
    private final TodoContentRecordDao todoContentRecordDao;

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<TodoRecordDto> findTodoContentRecords(String todoId){
        Todo todo = todoDao.findById(todoId)
                .orElseThrow(() -> new CustomException(ErrorCode.TODO_NOT_FOUND));
        List<TodoContent> todoContents = todoContentDao.findTodoContentsByTodo(todo);
        if(todoContents.isEmpty())
            return Collections.emptyList();

        List<TodoRecordDto> todoContentRecordDtos = new ArrayList<>();
        for(TodoContent todoContent: todoContents){
            List<TodoContentRecord> todoContentRecords = todoContentRecordDao.findTodoContentRecordsByTodoContent(todoContent);
            if(todoContentRecords.isEmpty())
                continue;

            for(TodoContentRecord todoContentRecord: todoContentRecords){
                todoContentRecordDtos.add(TodoRecordDto.todoContentRecordToDto(todoContentRecord));
            }
        }

        return todoContentRecordDtos;
    }


}
