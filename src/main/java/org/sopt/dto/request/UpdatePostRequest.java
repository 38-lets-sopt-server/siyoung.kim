package org.sopt.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdatePostRequest(
        @Schema(description = "수정할 게시글 제목(최대 길이 50자)", example = "게시글 제목 수정해볼게요")
        String title,

        @Schema(description = "수정할 게시글 내용(글자수 1자 이상)", example = "이거 게시글 내용 수정한거에요!!")
        String content
) {

}