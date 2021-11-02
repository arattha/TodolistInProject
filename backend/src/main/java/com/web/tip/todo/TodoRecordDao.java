package com.web.tip.todo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRecordDao extends JpaRepository<TodoRecord, String> {
}
