package org.sopt.global.response;

import org.sopt.global.code.ErrorCode;
import org.sopt.global.code.SuccessCode;
import org.sopt.global.exception.BaseException;
import org.springframework.http.ResponseEntity;

// 클라이언트가 서버로부터 받은 응답을 한 번에 처리할 수 있는 공통 응답 객체
public record BaseResponse<T>(
        boolean success,
        int status,
        String code,
        String message,
        T data
) {
    // 1. 성공 응답 (ResponseEntity까지 포함)
    public static <T> ResponseEntity<BaseResponse<T>> success(SuccessCode sc, T data) {
        return ResponseEntity
                .status(sc.getHttpStatus())
                .body(new BaseResponse<>(true, sc.getStatus(), sc.getCode(), sc.getMessage(), data));
    }

    // 2. 데이터가 없는 성공 응답 (삭제 같은거)
    public static ResponseEntity<BaseResponse<Void>> success(SuccessCode sc) {
        return ResponseEntity
                .status(sc.getHttpStatus())
                .body(new BaseResponse<>(true, sc.getStatus(), sc.getCode(), sc.getMessage(), null));
    }

    // 3. 에러일 때
    public static ResponseEntity<BaseResponse<Void>> error(BaseException e) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(new BaseResponse<>(false, errorCode.getStatus(),
                        errorCode.getCode(), errorCode.getMessage(), null));
    }
}
