package com.jobdam.common.exception;

public class LimitExceededException extends RuntimeException {

    public LimitExceededException(String message) {
        super(message != null ? message : "제한을 초과했습니다.");
    }

}