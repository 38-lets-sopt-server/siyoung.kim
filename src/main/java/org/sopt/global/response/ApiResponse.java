package org.sopt.global.response;

import org.sopt.global.code.ErrorCode;
import org.sopt.global.code.SuccessCode;
import org.sopt.global.exception.BaseException;

// 클라이언트가 서버로부터 받은 응답을 한 번에 처리할 수 있는 공통 응답 객체
public record ApiResponse<T>(
        boolean success,
        String code,
        String message,
        T data
) {
    public static <T> ApiResponse<T> success(SuccessCode sc, T data) {
        return new ApiResponse<>(true, sc.getCode(), sc.getMessage(), data);
    }

    public static ApiResponse<Void> error(BaseException e) {
        return new ApiResponse<>(false, e.getErrorCode().getCode(),
                e.getErrorCode().getMessage(), null);
    }
}
