package org.sopt.domain;

import jakarta.persistence.*;

@Entity
public class Post {

    @Id // 앞에서 배운 PK
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;          // 게시글 상세 화면 — 특정 게시글 식별용

    private String title;     // 목록, 상세, 글쓰기 화면 — 제목
    private String content;   // 목록(미리보기), 상세(전체) 화면 — 내용
    private String author;    // 목록, 상세 화면 — 글쓴이
    private String createdAt; // 목록, 상세 화면 — 작성 시각

    private BoardType boardType; // 게시판 종류 필드 추가

    @ManyToOne(fetch = FetchType.LAZY)  // User : Post = 1 : N
    @JoinColumn(name = "user_id")       // post 테이블에 user_id FK 컬럼이 생겨요
    private User user;

    protected Post() {}  // JPA 기본 생성자

    public Post(Long id, String title, String content, String author,
                String createdAt, BoardType boardType,
                User user) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdAt = createdAt;
        this.boardType = boardType;
        this.user = user;
    }

    public Post(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getAuthor() { return author; }
    public String getCreatedAt() { return createdAt; }
    public BoardType getBoardType() { return boardType; }

    public  User getUser() { return user; }


    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getInfo() {
        return "[" + id + "] " + title + " - " + author + " (" + createdAt + ")\n" + content;
    }
}
