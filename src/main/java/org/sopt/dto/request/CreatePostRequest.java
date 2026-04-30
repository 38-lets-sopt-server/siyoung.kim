package org.sopt.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

// 게시글 작성 요청 (클라이언트 → 서버)
public record CreatePostRequest(
        @Schema(description = "게시글 제목(최대 길이 50자)", example = "이거 제목입니다")
        String title,

        @Schema(description = "게시글 내용(글자수 1자 이상", example = "글 내용입니다.")
        String content,

        @Schema(description = "게시판 종류입니다. 종류는 free, hot, secret", example = "free")
        String boardType,

        @Schema(description = "작성자의 id입니다", example = "1")
        Long userId
) {}