package org.sopt.global.exception;

import org.sopt.global.code.ErrorCode;

public class PostInvalidInputException extends BaseException{
    public PostInvalidInputException() {
        super(ErrorCode.POST_INVALID_TITLE);
    }
}
