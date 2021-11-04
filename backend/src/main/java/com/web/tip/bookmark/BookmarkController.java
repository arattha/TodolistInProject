package com.web.tip.bookmark;

import com.web.tip.BasicResponse;
import com.web.tip.member.request.SignUpRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ApiResponses(value = {@ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class)})

@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/bookmark")
public class BookmarkController {

    BookmarkService bookmarkService;

    @PostMapping()
    @ApiOperation(value = "북마크 생성")
    public ResponseEntity<BasicResponse> addBookmark(@RequestBody BookmarkDto bookmarkDto) {
        log.info("add Bookmark");
        boolean bookmarkResult = bookmarkService.addBookmark(bookmarkDto);

        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "Success";
        result.object = bookmarkResult;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping()
    @ApiOperation(value = "북마크 삭제")
    public ResponseEntity<BasicResponse> deleteBookmark(@RequestBody BookmarkDto bookmarkDto) {
        log.info("delete Bookmark");
        boolean bookmarkResult = bookmarkService.deleteBookmark(bookmarkDto);

        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "Success";
        result.object = bookmarkResult;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
