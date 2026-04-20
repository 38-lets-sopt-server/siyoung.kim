package org.sopt.global.exception;

import org.sopt.global.code.ErrorCode;
import org.sopt.global.response.ApiResponse;
import org.sopt.global.response.ErrorResponse;
import org.sopt.exception.PostNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    // 커스텀 예외 handler, PostNotFoundException → 404
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handlePostNotFound(ErrorCode e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(ApiResponse.error(e));
    }

    // 유효성 검증 실패 → 400
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
    }

    // 예상치 못한 모든 예외 → 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("서버 내부 오류가 발생했습니다."));
    }
}