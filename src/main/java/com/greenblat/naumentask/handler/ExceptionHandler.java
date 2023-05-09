package com.greenblat.naumentask.handler;

import com.greenblat.naumentask.exception.EmptyNameException;
import com.greenblat.naumentask.model.dto.ExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = EmptyNameException.class)
    public String handleException(EmptyNameException e, Model model) {
        ExceptionDto exceptionDto = new ExceptionDto(e.getMessage(), LocalDateTime.now(), HttpStatus.BAD_REQUEST);
        model.addAttribute("exception", exceptionDto);
        return "error/exception-page";
    }
}
