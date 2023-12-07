package com.example.lms.global.exception;

import lombok.Getter;

@Getter
public class ClientException extends RuntimeException {

    public ClientException(String message) {
        super(message);
    }

}
