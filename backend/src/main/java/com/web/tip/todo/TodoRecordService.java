package com.web.tip.todo;

import com.web.tip.error.CustomException;
import com.web.tip.error.ErrorCode;
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
public class TodoRecordService {
    private final TodoDao todoDao;
    private final TodoRecordDao todoRecordDao;

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<TodoRecordDto> findTodoRecordsByTodoId(String todoId) {
        Todo todo = todoDao.findById(todoId)
                .orElseThrow(() -> new CustomException(ErrorCode.TODO_NOT_FOUND));

        List<TodoRecord> todoRecords = todoRecordDao.findTodoRecordsByTodo(todo);
        if (todoRecords.isEmpty())
            return Collections.emptyList();

        List<TodoRecordDto> todoRecordDtos = new ArrayList<>();
        for (TodoRecord todoRecord : todoRecords) {
            todoRecordDtos.add(TodoRecordDto.todoRecordToDto(todoRecord));
        }
        return todoRecordDtos;
    }

}
