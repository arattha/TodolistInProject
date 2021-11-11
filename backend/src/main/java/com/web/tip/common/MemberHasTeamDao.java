package com.web.tip.common;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface MemberHasTeamDao extends JpaRepository<MemberHasTeam, String> {

    List<MemberHasTeam> findMemberHasTeamByMemberId(String memberId);
    Optional<MemberHasTeam> findMemberHasTeamByMemberIdAndTeamId(String memberId,String teamId);
    List<MemberHasTeam> findByTeamIdInAndIsUse(ArrayList<String> teamIds, boolean isUse);
    List<MemberHasTeam> findByTeamId(String teamId);
}
