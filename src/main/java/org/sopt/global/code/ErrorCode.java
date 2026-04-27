package org.sopt.global.code;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    //POST 관련 에러
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST_001", "게시글을 찾을 수 없습니다"),
    POST_INVALID_TITLE(HttpStatus.BAD_REQUEST, "POST_002", "게시글 제목은 1~50자 이하입니다"),
    POST_INVALID_CONTENT(HttpStatus.BAD_REQUEST, "POST_003", "내용은 필수입니다"),
    POST_INVALID_BOARD(HttpStatus.BAD_REQUEST, "POST_004", "게시판 선택은 필수입니다"),

    POST_INVALID_PAGINATION(HttpStatus.BAD_REQUEST, "POST_005", "잘못된 페이지네이션입니다"),

    //BOARD 관련 에러
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "BOARD_001", "존재하지 않는 게시판입니다"),


    // User 관련 에러
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_001", "사용자를 찾을 수 없습니다"),

    // 전체 에러
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "GLOBAL_001", "잘못된 요청입니다"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "GLOBAL_002", "인증이 필요합니다"),
    FORBIDDEN( HttpStatus.FORBIDDEN, "GLOBAL_003","권한이 없습니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "GLOBAL_004", "서버 내부 오류가 발생했습니다");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
