package com.greenblat.naumentask.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class EmptyFormException extends RuntimeException {
    public EmptyFormException(String message) {
        super(message);
    }
}
