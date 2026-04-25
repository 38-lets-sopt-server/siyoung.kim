package org.sopt.dto.response;

import org.sopt.domain.Post;

// 게시글 조회 응답
// Post 객체를 그대로 노출하지 않고, 보여줄 것만 골라서 반환
public record PostResponse(
        Long id,
        String title,
        String content,
        String author,
        String createdAt,
        String boardType
){
    // Post 객체를 PostResponse로 변환하는 정적 팩토리 메서드
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor(),
                post.getCreatedAt(),
                post.getBoardType().getLabel()
        );
    }
}
