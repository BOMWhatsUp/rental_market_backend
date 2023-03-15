package com.bom.rentalmarket.UserController.domain.exception;

public class ExistsNickNameException extends RuntimeException {
    public ExistsNickNameException(String s) {
        super(s);
    }
}
