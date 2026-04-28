package org.sopt.global.exception;

import org.sopt.global.code.ErrorCode;
import org.sopt.global.response.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    // 커스텀 예외 handler
    // BaseException 으로 모든 예외 다 잡아서 공통 응답으로 반환할 수 있게 만들기
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponse<Void>> handleBaseException(BaseException e) {
        return BaseResponse.error(e);
    }

    // 잘못된 요청
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public  ResponseEntity<BaseResponse<Void>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return BaseResponse.error(new BaseException(ErrorCode.INVALID_INPUT));
    }

    // 예상치 못한 모든 예외 → 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Void>> handleException(Exception e) {
        return BaseResponse.error(new BaseException(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}