package com.web.tip.project;

import com.web.tip.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectDao extends JpaRepository<Project, String> {

    Optional<Project> findProjectById(String id);
    Optional<Project> findProjectByName(String name);
    
}
