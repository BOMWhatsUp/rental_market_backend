package com.bom.rentalmarket.member.domain.exception;

public class ExistsNickNameException extends RuntimeException {
    public ExistsNickNameException(String s) {
        super(s);
    }
}
