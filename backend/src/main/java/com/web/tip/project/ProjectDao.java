package com.web.tip.project;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectDao extends JpaRepository<Project, String> {

    Optional<Project> findProjectById(String id);
    List<Project> findProjectByIdInAndIsDone(List<String> ids, boolean isDone);
    Optional<Project> findProjectByName(String name);
    boolean existsByName(String projectName);
}
