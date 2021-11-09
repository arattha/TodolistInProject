package com.web.tcp.bookmark;

import com.web.tcp.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkDao extends JpaRepository<Bookmark, String> {

    List<Member> findMemberByTodoIdAndIsUseIsTrue(String memberId);
}
