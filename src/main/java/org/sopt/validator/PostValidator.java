package org.sopt.validator;

import org.sopt.global.code.ErrorCode;
import org.sopt.global.exception.BaseException;

// Post의 title과 content에 대한 유효성 검증하는 클래스
public class PostValidator {

    public static void validateTitle(String title) {
        if(title == null || title.isBlank() || title.length() > 50) {
            throw new BaseException(ErrorCode.POST_INVALID_TITLE);
        }
    }

    public static void validateContent(String content) {
        if(content == null || content.isBlank()) {
            throw new BaseException(ErrorCode.POST_INVALID_CONTENT);
        }
    }
}
