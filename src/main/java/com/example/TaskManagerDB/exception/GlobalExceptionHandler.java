package com.example.TaskManagerDB.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.webmvc.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final DefaultErrorAttributes errorAttributes;

    public GlobalExceptionHandler(DefaultErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ProblemDetail taskNotFoundHandler(TaskNotFoundException e, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                e.getMessage()
        );
        problem.setTitle("Task non trovato");
        problem.setInstance(java.net.URI.create(request.getRequestURI()));
        return problem;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleUnreadableMessage(HttpMessageNotReadableException e, HttpServletRequest request) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "corpo del Task mancante o contenente valori non validi"
        );
        problem.setTitle("Invalid request body");
        problem.setInstance(java.net.URI.create(request.getRequestURI()));
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail argumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();

        e.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage())
                );

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "sono stati riscontrati uno o più errori di validazione"
        );
        problem.setTitle("Validation failed");
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("errors", errors);
        return problem;
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ProblemDetail methodArgumentNotValid(HandlerMethodValidationException e, HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();

        e.getParameterValidationResults().forEach(result -> {
            result.getResolvableErrors().forEach(error ->
                    errors.put(
                            result.getMethodParameter().getParameterName(),
                            error.getDefaultMessage()
                    )
            );
        });

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Sono stati riscontrati uno o più errori di validazione."
        );
        problem.setTitle("Validation failed");
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("errors", errors);
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail internalServerError(Exception e, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Errore inaspettato"
        );
        problem.setTitle("Unexpected server error");
        problem.setInstance(java.net.URI.create(request.getRequestURI()));
        logger.error("Unexpected server error", e);
        return problem;
    }
}
