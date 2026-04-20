package org.sopt.global.exception;

import org.sopt.global.code.ErrorCode;

// 존재하지 않는 board type 을 찾을 때
public class BoardTypeNotFoundException extends BaseException {
    public BoardTypeNotFoundException() {
        super(ErrorCode.BOARD_NOT_FOUND);
    }
}
