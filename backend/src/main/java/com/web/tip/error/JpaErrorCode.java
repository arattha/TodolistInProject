package com.web.tip.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JpaErrorCode {

    SAVE_TEAM_ERROR( "팀 저장이 실패했습니다."),
    SAVE_PROJECT_ERROR("프로젝트 저장이 실패했습니다."),
    SAVE_TODO_ERROR("할일 저장이 실패했습니다."),
    ;

    private final String errorDetail;

}
