package com.bom.rentalmarket.UserController.domain.exception;

public class NotMatchPasswordException extends RuntimeException {
    public NotMatchPasswordException (String s) {
        super(s);
    }
}
