package com.web.tcp.bookmark;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkDao extends JpaRepository<Bookmark, String> {

    List<String> findMemberIdByTodoId(String memberId);
}
