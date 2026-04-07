package org.sopt.validator;

public class PostValidator {

    public void validateTitle(String title) {
        if(title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목을 입력하세요.");
        }
        if(title.length() > 50) {
            throw new IllegalArgumentException("제목은 50자 이하여야합니다.");
        }
    }

    public void validateContent(String content) {
        if(content == null || content.isBlank()) {
            throw new IllegalArgumentException("내용을 입력해주세요.");
        }
    }
}
