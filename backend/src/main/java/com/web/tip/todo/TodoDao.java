package com.web.tip.todo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoDao extends JpaRepository<Todo, String> {
    List<Todo> findTodosByProjectIdAndMemberId(String projectId, String memberId);
}
