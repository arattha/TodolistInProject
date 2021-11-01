package com.web.tcp.todo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoDao extends JpaRepository<Todo, String> {

    Optional<Todo> findTodoById(String id);
    Optional<Todo> findTodoByTitle(String title);
    List<Todo> findTodosByProjectId(String projectId);
}
