package com.web.tip.todo;

import com.web.tip.project.Project;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TodoDao extends JpaRepository<Todo, String> {
    @Query("SELECT t.status from Todo t where t.project = :#{#project}")
    List<String> findStatusByProject(@Param(value = "project") Project project);
}
