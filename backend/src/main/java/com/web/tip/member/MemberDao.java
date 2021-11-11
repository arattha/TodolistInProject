package com.web.tip.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberDao extends JpaRepository<Member, String> {
    Optional<Member> findMemberById(String id);

    Optional<Member> findMemberByNickname(String nickname);
    List<Member> findMemberByIsUse(boolean isUse);
    boolean existsByNickname(String nickname);

}
