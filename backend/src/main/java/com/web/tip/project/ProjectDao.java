package com.web.tip.project;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectDao extends JpaRepository<Project, String> {

    Optional<Project> findProjectById(String id);
}
