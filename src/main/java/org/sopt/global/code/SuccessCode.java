package org.sopt.global.code;

import org.springframework.http.HttpStatus;

public enum SuccessCode {
    // SUCCESS_OK 로 조회, 생성, 업데이트, 삭제 요청 처리(status: 200)
    SUCCESS_OK(HttpStatus.OK, "SUCCESS_01", "요청 성공"),

    // status: 201
    SUCCESS_CREATED(HttpStatus.CREATED, "SUCCESS_02", "생성 완료");


    private final HttpStatus status;
    private final String code;
    private final String message;

    SuccessCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus() { return status; }

    public String getCode() { return code; }

    public String getMessage() { return message; }
}
