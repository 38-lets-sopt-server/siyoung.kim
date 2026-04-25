package org.sopt.domain;

import org.sopt.global.exception.BoardTypeNotFoundException;

public enum BoardType {
    FREE("free","자유게시판"),
    HOT("hot","인기게시판"),
    SECRET("secret","비밀게시판");

    private final String boardName;
    private final String label;

    BoardType(String boardName, String label) {
        this.boardName = boardName;
        this.label = label;
    }

    public String getBoardName() {
        return boardName;
    }

    public String getLabel() {
        return label;
    }

    // 나중에 uri 에서 /free 이렇게 들어오면 그걸 맞는걸로 변환하기 위한 메소드
    public static BoardType from(String boardName) {

        for(BoardType b : values()) {
            if(b.getBoardName().equals(boardName)) {
                return b;
            }
        }

        throw new BoardTypeNotFoundException();
    }
}
