package com.web.tip.todo.content;

import com.web.tip.todo.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoContentDao extends JpaRepository<TodoContent, String> {
    List<TodoContent> findTodoContentsByTodo(Todo todo);
}
