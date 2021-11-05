package com.web.tip;

import com.web.tip.todo.Todo;
import com.web.tip.todo.TodoDao;
import com.web.tip.todo.content.TodoContent;
import com.web.tip.todo.content.TodoContentDto;
import com.web.tip.todo.content.TodoContentService;
import com.web.tip.todo.content.record.TodoContentRecord;
import com.web.tip.todo.content.record.TodoContentRecordDao;
import com.web.tip.todo.content.request.ContentModifyRequest;
import com.web.tip.todo.content.request.ContentRequest;
import com.web.tip.todo.content.url.TodoUrl;
import com.web.tip.todo.content.url.TodoUrlDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class TodoContentTest {
    @Autowired
    private TodoDao todoDao;
    @Autowired
    private TodoContentService todoContentService;
    @Autowired
    private TodoUrlDao todoUrlDao;
    @Autowired
    private TodoContentRecordDao todoContentRecordDao;

    @Test
    void testCreateTodoContent(){
        String todoId = "1";
        String contents = "https://www.google.com/\n" +
                "\n" +
                "[hi](http://www.naver.com)";
        String memberId = "8479097887171";

        TodoContentDto todoContentDto = todoContentService.addTodoContent(ContentRequest.builder()
                .todoId(todoId)
                .contents(contents)
                .memberId(memberId)
                .build());
    }

    @Test
    void testModifyTodoContent(){
        String todoContentId = "9341798090894";
        String contents = "https://www.google.com/\n" +
                "\n" +
                "[hi](http://www.naver.com)";

        TodoContentDto todoContentDto = todoContentService.modifyTodoContent(ContentModifyRequest.builder()
                .id(todoContentId)
                .contents(contents)
                .memberId("8479097887171")
                .build());

        List<TodoUrl> todoUrls = todoUrlDao.findTodoUrlsByTodoContent(TodoContent.builder().id(todoContentId).build());
        Assertions.assertEquals(todoUrls.size(), 2, "url 개수가 다릅니다");
    }




}
