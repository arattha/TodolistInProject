package com.web.tip.todo.content.url;

import com.web.tip.error.CustomException;
import com.web.tip.error.ErrorCode;
import com.web.tip.todo.Todo;
import com.web.tip.todo.TodoDao;
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
public class TodoUrlService {
    private final TodoUrlDao todoUrlDao;
    private final TodoContentDao todoContentDao;
    private final TodoDao todoDao;

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<TodoUrlDto> findUrlsByTodoContent(String todoId) {
        Todo todo = todoDao.findById(todoId)
                .orElseThrow(() -> new CustomException(ErrorCode.TODO_NOT_FOUND));
        List<TodoContent> todoContents = todoContentDao.findTodoContentsByTodo(todo);
        if (todoContents.isEmpty())
            return Collections.emptyList();

        List<TodoUrlDto> todoUrlDtos = new ArrayList<>();
        for (TodoContent todoContent : todoContents) {
            if (!todoContent.isUse())
                continue;

            List<TodoUrl> todoUrls = todoUrlDao.findTodoUrlsByTodoContent(todoContent);
            String writer = todoContent.getMember().getName();
            for (TodoUrl url : todoUrls) {
                todoUrlDtos.add(TodoUrlDto.entityToDto(url, writer));
            }
        }

        return todoUrlDtos;
    }
}
