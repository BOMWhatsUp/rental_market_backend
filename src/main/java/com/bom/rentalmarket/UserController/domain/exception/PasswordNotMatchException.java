package com.bom.rentalmarket.UserController.domain.exception;

public class PasswordNotMatchException extends RuntimeException {
    public PasswordNotMatchException(String s) {
        super(s);
    }
}
