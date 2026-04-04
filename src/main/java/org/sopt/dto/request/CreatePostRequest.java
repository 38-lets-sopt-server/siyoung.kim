package org.sopt.dto.request;

// 게시글 작성 요청 (클라이언트 → 서버)
public class CreatePostRequest {
    public String title;
    public String content;
    public String author;

    public CreatePostRequest(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }
}