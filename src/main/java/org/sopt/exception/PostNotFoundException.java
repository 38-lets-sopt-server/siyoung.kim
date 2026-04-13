package org.sopt.exception;

// 존재하지 않는 id에 대한 Post 객체 접근 시 보낼 커스텀 Exception
public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException() {
        super("존재하지 않는 게시글입니다");
    }
}
