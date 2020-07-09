package com.bordozer.battleship.multiplayer.controller;

import com.bordozer.battleship.multiplayer.exception.GameNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = GameNotFoundException.class)
    protected ResponseEntity<Object> handleInternalError(final GameNotFoundException ex, final WebRequest request) {
        return handleExceptionInternal(ex, ErrorResponse.of("gameId", ex.getGameId()), new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @Getter
    @RequiredArgsConstructor
    @ToString
    public static class ErrorResponse {
        private final Map<String, String> errors;

        static ErrorResponse of(final String key, final String error) {
            return new ErrorResponse(Collections.singletonMap(key, error));
        }

        static ErrorResponse of(final Map<String, String> errors) {
            return new ErrorResponse(errors);
        }
    }
}
