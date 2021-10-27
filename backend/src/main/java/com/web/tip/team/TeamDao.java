package com.web.tip.team;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamDao extends JpaRepository<Team, String> {
    List<Team> findTeamByProjectId(String ProjectId);
}
