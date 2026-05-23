package org.sopt.global.common.response;

import org.sopt.global.common.code.ErrorCode;
import org.sopt.global.common.code.SuccessCode;
import org.sopt.global.exception.BaseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

// 클라이언트가 서버로부터 받은 응답을 한 번에 처리할 수 있는 공통 응답 객체
public record BaseResponse<T>(
        boolean success,
        int status,
        String code,
        String message,
        T data
) {
    // 성공 응답 (ResponseEntity까지 포함)
    public static <T> ResponseEntity<BaseResponse<T>> success(SuccessCode sc, T data) {
        return ResponseEntity
                .status(sc.getHttpStatus())
                .body(new BaseResponse<>(true, sc.getStatus(), sc.getCode(), sc.getMessage(), data));
    }

    // 데이터가 없는 성공 응답 (삭제 같은거)
    public static ResponseEntity<BaseResponse<Void>> success(SuccessCode sc) {
        return ResponseEntity
                .status(sc.getHttpStatus())
                .body(new BaseResponse<>(true, sc.getStatus(), sc.getCode(), sc.getMessage(), null));
    }

    // 로그인 인증 성공 후 쿠키 내려줄 때
    public static <T> ResponseEntity<BaseResponse<T>> success(
            SuccessCode sc,
            T data,
            ResponseCookie cookie
    ) {
        // cookie 는 refreshToken 값이 들어올거임. refreshToken 은 HttpOnly 쿠키에 저장해야한다!!
        return ResponseEntity
                .status(sc.getHttpStatus())
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new BaseResponse<>(true, sc.getStatus(), sc.getCode(), sc.getMessage(), data));
    }

    // 에러일 때
    public static ResponseEntity<BaseResponse<Void>> error(BaseException e) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(new BaseResponse<>(false, errorCode.getStatus(),
                        errorCode.getCode(), errorCode.getMessage(), null));
    }


}
