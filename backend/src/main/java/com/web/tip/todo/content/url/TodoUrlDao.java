package com.web.tip.todo.content.url;

import com.web.tip.todo.content.TodoContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoUrlDao extends JpaRepository<TodoUrl, String> {
    List<TodoUrl> findTodoUrlsByTodoContent(TodoContent todoContent);
}
