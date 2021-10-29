package com.web.tip.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public class JpaException extends RuntimeException{
    private final JpaErrorCode errorCode;
}
