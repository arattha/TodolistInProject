package com.web.tip.todo.content.record;

import com.web.tip.todo.content.TodoContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoContentRecordDao extends JpaRepository<TodoContentRecord, String> {
    List<TodoContentRecord> findTodoContentRecordsByTodoContent(TodoContent todoContent);
}
