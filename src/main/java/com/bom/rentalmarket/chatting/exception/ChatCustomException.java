package com.bom.rentalmarket.chatting.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatCustomException extends RuntimeException{
    private final ErrorCode errorCode;
    private final int status;
    private static final ObjectMapper mapper = new ObjectMapper();

    public ChatCustomException(ErrorCode errorCode) {
        super(errorCode.getContent());
        this.errorCode = errorCode;
        this.status = errorCode.getStatus().value();
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class CustomExceptionResponse {
        private int status;
        private String code;
        private String message;
    }
}
