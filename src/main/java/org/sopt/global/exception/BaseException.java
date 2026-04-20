package org.sopt.global.exception;

import org.sopt.global.code.ErrorCode;

public class BaseException extends RuntimeException{

    private final ErrorCode e;

    public BaseException(ErrorCode e) {
        super(e.getMessage());
        this.e = e;
    }

    public ErrorCode getErrorCode() {
        return e;
    }
}
