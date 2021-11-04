package com.web.tcp.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberDao extends JpaRepository<Member, String> {
    Optional<Member> findMemberById(String id);

    boolean existsByNickname(String nickname);
}
