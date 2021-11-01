package com.web.tcp.todo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoDao extends JpaRepository<Todo, String> {

    List<Todo> findTodoByProjectId(String project_id);
}
