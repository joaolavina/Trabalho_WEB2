package com.web2.trabalhofinal.exceptions;

public class CustomConstraintViolantionException extends RuntimeException {
    public CustomConstraintViolantionException(String pMessage) {
        super(pMessage);
    }
}
