package com.web.tip.todo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRecordDao extends JpaRepository<TodoRecord, String> {
    List<TodoRecord> findTodoRecordsByTodo(Todo todo);
}
