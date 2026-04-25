package org.sopt.global.exception;

import org.sopt.global.code.ErrorCode;

// 존재하지 않는 id에 대한 Post 객체 접근 시 보낼 커스텀 Exception
// 확장성을 위해 RuntimeException 상속에서 BaseException 상속하도록!
public class PostNotFoundException extends BaseException {

    public PostNotFoundException() {
        super(ErrorCode.POST_NOT_FOUND);
    }

}
