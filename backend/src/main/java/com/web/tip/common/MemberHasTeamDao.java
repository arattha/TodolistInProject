package com.web.tip.common;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberHasTeamDao extends JpaRepository<MemberHasTeam, String> {

    List<MemberHasTeam> findMemberHasTeamByMemberId(String memberId);

}
