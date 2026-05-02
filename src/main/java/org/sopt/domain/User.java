package org.sopt.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "users")  // "user"는 SQL 예약어라 테이블명을 변경해요
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    private String email;

    protected User() {}

    public User(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }

    public Long getId() {
        return this.id;
    }

    public String getNickname() {
        return this.nickname;
    }

    public String getEmail() {
        return this.email;
    }
}