package com.web.tip.common;

import com.web.tip.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface MemberHasTeamDao extends JpaRepository<MemberHasTeam, String> {

    List<MemberHasTeam> findMemberHasTeamByMemberId(String memberId);
    Optional<MemberHasTeam> findMemberHasTeamByMemberIdAndTeamId(String memberId,String teamId);
    List<MemberHasTeam> findByTeamIdInAndIsUse(ArrayList<String> teamIds, boolean isUse);
    List<MemberHasTeam> findByTeamId(String teamId);

    @Query("select mt from MemberHasTeam mt where mt.memberId = :memberId AND mt.teamId in :teamIds")
    Optional<MemberHasTeam> findMemberHasTeamByMemberIdAndTeamIdIn(String memberId, List<String> teamIds);
}
