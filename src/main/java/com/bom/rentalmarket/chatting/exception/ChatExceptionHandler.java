package com.bom.rentalmarket.chatting.exception;

import com.bom.rentalmarket.chatting.exception.ChatCustomException.CustomExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ChatExceptionHandler {
    @ExceptionHandler({ChatCustomException.class})
    public ResponseEntity<CustomExceptionResponse> exceptionHandler(final ChatCustomException exception) {
        return ResponseEntity
            .status(exception.getStatus())
            .body(ChatCustomException.CustomExceptionResponse.builder()
                .message(exception.getMessage())
                .code(exception.getErrorCode().name())
                .status(exception.getStatus())
                .build());
    }
}
