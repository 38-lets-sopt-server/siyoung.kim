package org.sopt.dto.response;

// 클라이언트가 서버로부터 받은 응답을 한 번에 처리할 수 있는 공통 응답 객체
public class ApiResponse<T> {
    private final boolean success;
    private final String message;
    private final T data; // 제네릭 타입 T를 활용해서 어떤 데이터든 담을 수 있게

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> failure(String message) {
        return new ApiResponse<>(false, message, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
