package com.example.lms.global.exception;

public class JwtException extends RuntimeException{
    public JwtException(String message) {
        super(message);
    }
}
