package com.skillbox.socialnetwork.main.controller;

import com.skillbox.socialnetwork.main.dto.universal.BaseResponseDto;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionController extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = { ExpiredJwtException.class })
    protected ResponseEntity<Object> handleExpiredJwtException(
            ExpiredJwtException ex,
            WebRequest request
    ) {
        log.warn(ex.getMessage());
        return handleExceptionInternal(
                ex,
                new BaseResponseDto(),
                new HttpHeaders(),
                HttpStatus.OK,
                request
        );
    }

}
