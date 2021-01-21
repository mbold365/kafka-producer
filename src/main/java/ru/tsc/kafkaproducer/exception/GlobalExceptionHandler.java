package ru.tsc.kafkaproducer.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;

import java.io.IOException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseStatusExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public void illegalArgumentException(ServerHttpResponse response) {
        log.info("Handled IllegalArgumentException, sending BAD_REQUEST as response");
        response.setStatusCode(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public void ioException(ServerHttpResponse response) {
        log.info("Handled IOException, sending BAD_REQUEST as response");
    }
}
