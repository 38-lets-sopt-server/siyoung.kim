package org.sopt.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// 게시글 작성 요청 (클라이언트 → 서버)
public record CreatePostRequest(
        @Schema(description = "게시글 제목(최대 길이 50자)", example = "이거 제목입니다")
        @NotBlank(message = "제목은 필수입니다.")
        @Size(max = 50, message = "제목은 50자를 초과할 수 없습니다.")
        String title,

        @Schema(description = "게시글 내용(글자수 1자 이상", example = "글 내용입니다.")
        @NotBlank(message = "내용은 필수입니다.")
        @Size(max = 500, message = "내용은 500자를 초과할 수 없습니다.")
        String content,

        @Schema(description = "게시판 종류입니다. 종류는 free, hot, secret", example = "free")
        @NotBlank(message = "게시글 작성은 필수입니다.")
        String boardType,

        @Schema(description = "작성자의 id입니다", example = "1")
        @NotNull(message = "사용자 id는 필수입니다.") // 이러한 숫자 데이터타입은 @NotNull로 유효성 검증
        Long userId
) {}