package com.web.tip.team;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamDao extends JpaRepository<Team, String> {
    List<Team> findTeamByProjectIdAndIsUse(String ProjectId,boolean isUse);
    Optional<Team> findTeamById(String id);
}
