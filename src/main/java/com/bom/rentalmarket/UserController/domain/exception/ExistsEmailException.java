package com.bom.rentalmarket.UserController.domain.exception;

public class ExistsEmailException extends RuntimeException {
    public ExistsEmailException(String s) {
        super(s);
    }
}
