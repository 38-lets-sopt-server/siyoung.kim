package org.sopt.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePostRequest(
        @Schema(description = "수정할 게시글 제목(최대 길이 50자)", example = "게시글 제목 수정해볼게요")
        @NotBlank(message = "제목은 필수입니다.")
        @Size(max = 50, message = "제목은 50자를 초과할 수 없습니다.")
        String title,

        @Schema(description = "수정할 게시글 내용(글자수 1자 이상)", example = "이거 게시글 내용 수정한거에요!!")
        @NotBlank(message = "내용은 필수입니다.")
        @Size(max = 500, message = "내용은 500자를 초과할 수 없습니다.")
        String content
) {

}