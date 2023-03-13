package com.bom.rentalmarket.member.domain.exception;

public class ExistsEmailException extends RuntimeException {
    public ExistsEmailException(String s) {
        super(s);
    }
}
