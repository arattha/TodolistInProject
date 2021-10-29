package com.web.tcp.todo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoDao extends JpaRepository<Todo, String> {

}
