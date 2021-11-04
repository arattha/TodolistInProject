package com.web.tip.bookmark;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface BookmarkDao extends JpaRepository<Bookmark, String> {
    Optional<Bookmark> findBookmarksByMemberIdAndTodoIdAndIsUse(String memberId, String todoId, boolean isUse);
    List<Bookmark> findBookmarksByMemberIdAndTodoIdInAndIsUse(String memberId, List<String> todoId, boolean isUse);
}

