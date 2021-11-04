package com.web.tip.bookmark;

import com.web.tip.error.CustomException;
import com.web.tip.error.ErrorCode;
import com.web.tip.error.JpaErrorCode;
import com.web.tip.error.JpaException;
import com.web.tip.util.IdGenerator;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookmarkService {

    BookmarkDao bookmarkDao;

    public boolean addBookmark(BookmarkDto bookmarkDto) {

        IdGenerator idGenerator = new IdGenerator();
        String id = idGenerator.generateId();
        while(bookmarkDao.existsById(id)){
            id = idGenerator.generateId();
        }

        try {

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
