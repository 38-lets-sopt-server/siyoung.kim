package org.sopt.dto.response;

import org.sopt.domain.Post;

import java.time.LocalDateTime;

// 게시글 조회 응답
// Post 객체를 그대로 노출하지 않고, 보여줄 것만 골라서 반환
public record PostResponse(
        String title,
        String author,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String boardType
){
    // Post 객체를 PostResponse로 변환하는 정적 팩토리 메서드
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getTitle(),
                // fetch join 쓰기 전에는 여기서 n+1 문제 생김
                post.getUser().getNickname(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getBoardType().getLabel()
        );
    }
}
