package com.greenblat.naumentask.model.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ExceptionDto(String message,
                           LocalDateTime localDateTime,
                           HttpStatus httpStatus) {

}
