package com.web.tip.mypage;

import com.web.tip.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberDetailDao extends JpaRepository<MemberDetail, String> {
    Optional<MemberDetail> findMemberDetailByMember(Member member);
    Optional<MemberDetail> findMemberDetailByMemberId(String memberId);
}
