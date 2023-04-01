package com.bom.rentalmarket.chatting.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    NOT_FOUND_CHATROOM(HttpStatus.NOT_FOUND, "존재하지 않는 채팅 방입니다."),
    REQUIRED_LOGIN(HttpStatus.BAD_REQUEST, "로그인이 필요합니다."),
    REQUIRED_JOIN_MEMBER(HttpStatus.BAD_REQUEST, "회원가입이 필요합니다."),
    NONE_EXISTENT_MEMBER(HttpStatus.NOT_FOUND, "존재하지 않는 회원 입니다.");

    private final HttpStatus status;
    private final String content;
}
