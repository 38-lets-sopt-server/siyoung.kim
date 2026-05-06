package org.sopt.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
// 이 SQLDelete 덕분에 postRepository.delete() 하면 soft delete 됨
@SQLDelete(sql = "UPDATE post SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Post extends BaseTimeEntity {

    @Id // 앞에서 배운 PK
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;          // 게시글 상세 화면 — 특정 게시글 식별용

    private String title;     // 목록, 상세, 글쓰기 화면 — 제목
    private String content;   // 목록(미리보기), 상세(전체) 화면 — 내용

    @Column(name = "board_type")
    private BoardType boardType; // 게시판 종류 필드 추가

    @ManyToOne(fetch = FetchType.LAZY)  // User : Post = 1 : N
    @JoinColumn(name = "user_id")       // post 테이블에 user_id FK 컬럼이 생겨요
    private User user;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    protected Post() {}  // JPA 기본 생성자

    public Post(String title, String content, BoardType boardType, User user) {
        this.title = title;
        this.content = content;
        this.boardType = boardType;
        this.user = user;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public BoardType getBoardType() { return boardType; }

    public User getUser() { return user; }


    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
