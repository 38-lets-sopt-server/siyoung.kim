package org.sopt.dto.request;

import org.sopt.domain.BoardType;
import org.sopt.domain.User;

// 게시글 작성 요청 (클라이언트 → 서버)
public record CreatePostRequest(
        String title,
        String content,
        String author,
        String boardType,
        Long userId
) {}