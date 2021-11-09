package com.web.tip.bookmark;

import com.web.tip.error.CustomException;
import com.web.tip.error.ErrorCode;
import com.web.tip.error.JpaErrorCode;
import com.web.tip.error.JpaException;
import com.web.tip.todo.Todo;
import com.web.tip.todo.TodoDao;
import com.web.tip.util.IdGenerator;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookmarkService {

    BookmarkDao bookmarkDao;
    TodoDao todoDao;

    public Object getBookmarkList(String projectId, String memberId) {

        List<Todo> todoList= todoDao.findTodosByProjectId(projectId);
        List<String> todoIdList = new ArrayList<>();
        todoList.forEach(v -> todoIdList.add(v.getId()));

        List<Bookmark> bookmarkList = bookmarkDao.findBookmarksByMemberIdAndTodoIdInAndIsUse(memberId,todoIdList,true);
        List<BookmarkDto> result = new ArrayList<>();

        bookmarkList.forEach(v -> result.add(
                new BookmarkDto(v.getMemberId(),v.getTodoId())
        ));

        return result;
    }

    public boolean addBookmark(BookmarkDto bookmarkDto) {

        IdGenerator idGenerator = new IdGenerator();
        String id = idGenerator.generateId();
        while(bookmarkDao.existsById(id)){
            id = idGenerator.generateId();
        }

        try {

            Optional<Bookmark> isBookmark = bookmarkDao.findBookmarksByMemberIdAndTodoIdAndIsUse(bookmarkDto.getMemberId(), bookmarkDto.getTodoId(),true); // 이미 있다면 더티리드
            if(isBookmark.isPresent()) {
                return true; //성공으로 간주하고 반환
            }

            Bookmark newBookmark = new Bookmark(id,bookmarkDto.getMemberId(),bookmarkDto.getTodoId(),true);
            bookmarkDao.save(newBookmark);

        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new JpaException(JpaErrorCode.SAVE_BOOKMARK_ERROR);
        }

        return true;
    }

    public boolean deleteBookmark(BookmarkDto bookmarkDto) {

        try {
            Bookmark delBookmark = bookmarkDao.findBookmarksByMemberIdAndTodoIdAndIsUse(bookmarkDto.getMemberId(), bookmarkDto.getTodoId(),true).orElseThrow(() -> new CustomException(ErrorCode.BOOKMARK_NOT_FOUND));
            delBookmark.deleteBookmark();
            bookmarkDao.save(delBookmark);
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new JpaException(JpaErrorCode.SAVE_BOOKMARK_ERROR);
        }

        return true;
    }

}
