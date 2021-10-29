package com.web.tip.error;

import com.web.tip.BasicResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<ErrorMessage> handleCustomException(CustomException e) {
        log.error("handle CustomException throw Exception : {}", e.getErrorCode());
        return ErrorMessage.errorToMessage(e.getErrorCode());
    }

    @ExceptionHandler(value = {JpaException.class})
    public ResponseEntity<BasicResponse> handleCustomException(JpaException e) {

        log.error("handle JpaExeption throw Exception : {}", e.getErrorCode());

        BasicResponse result = new BasicResponse();

        result.status =false;
        result.data = "fail";

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
