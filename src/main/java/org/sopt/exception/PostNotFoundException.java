package org.sopt.exception;

import org.sopt.global.code.ErrorCode;

// 존재하지 않는 id에 대한 Post 객체 접근 시 보낼 커스텀 Exception
public class PostNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public PostNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
