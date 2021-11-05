package com.web.tip.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /* 400 BAD_REQUEST : 잘못된 요청 */
    MEMBER_LOGOUT(HttpStatus.BAD_REQUEST, "로그아웃된 사용자입니다."),
    REMOVED_MEMBER_REQUEST(HttpStatus.BAD_REQUEST, "삭제된 사용자 입니다."),
    REMOVED_TEAM_REQUEST(HttpStatus.BAD_REQUEST, "삭제된 팀 입니다."),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    VALIDATION_FAIL(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음*/
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 팀을 찾을 수 없습니다."),
    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 프로젝트를 찾을 수 없습니다."),
    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 할 일을 찾을 수 없습니다."),
    TODO_CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 세부 사항을 찾을 수 없습니다."),
    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 즐겨찾기를 찾을 수 없습니다."),
    ALARM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 알람를 찾을 수 없습니다."),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 중복된 데이터 존재 */
    MEMBER_DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "이미 가입된 유저입니다."),
    NICKNAME_DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),


    /* 500 Internal Server Error : 서버 내 문제 발생 */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 알 수 없는 오류가 발생했습니다"),
    ;


    private final HttpStatus httpStatus;
    private final String errorDetail;
}
