package org.sopt.global.code;

import org.springframework.http.HttpStatus;

public enum SuccessCode {
    SUCCESS_OK(HttpStatus.OK, "SUCCESS_01", "요청 성공"),
    SUCCESS_CREATED(HttpStatus.CREATED, "SUCCESS_02", "생성 완료");

    // httpStatus 는 HttpStatus.OK
    private final HttpStatus httpStatus;
    // status 는 여기 내부에서 할당될 거임, ex. 200(숫자)
    private final int status;
    // code 는 내가 정한 코드명 ex. SUCCESS_01
    private final String code;
    private final String message;

    SuccessCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.status = httpStatus.value(); // httpStatus 로부터 값 가져와서 주입
        this.code = code;
        this.message = message;
    }

    public HttpStatus getHttpStatus() { return httpStatus; }
    public int getStatus() { return status; }
    public String getCode() { return code; }
    public String getMessage() { return message; }
}