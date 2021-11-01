package com.web.tcp.todoRecord;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRecordDao extends JpaRepository<TodoRecord, String> {

}
