package com.greenblat.naumentask.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class PersonFileNotFoundException extends RuntimeException {
    public PersonFileNotFoundException(String message) {
        super(message);
    }
}
