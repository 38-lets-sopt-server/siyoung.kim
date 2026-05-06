package org.sopt.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreateLikePostRequest(
        @NotNull(message = "사용자 id는 필수입니다")
        Long userId
) {
}
