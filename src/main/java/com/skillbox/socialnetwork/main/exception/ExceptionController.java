package com.skillbox.socialnetwork.main.exception;

import com.skillbox.socialnetwork.main.dto.universal.BaseResponse;
import com.skillbox.socialnetwork.main.dto.universal.MessageResponseDto;
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
    protected ResponseEntity<Object> handleUncaughtException(
            ExpiredJwtException ex,
            WebRequest request
    ) {
        log.warn(ex.getMessage());
        return handleExceptionInternal(
                ex,
                ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(new MessageResponseDto("ok"))),
                new HttpHeaders(),
                HttpStatus.OK,
                request
        );
    }

}
