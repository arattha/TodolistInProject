package com.web.tcp.team;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamDao extends JpaRepository<Team, String> {
    Optional<Team> findTeamById(String id);
    List<Team> findTeamByProjectId(String projectId);
}
