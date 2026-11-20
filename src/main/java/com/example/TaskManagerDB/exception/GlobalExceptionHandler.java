package com.example.TaskManagerDB.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(TaskNotFoundException.class)
    public ProblemDetail taskNotFoundHandler(TaskNotFoundException e, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                e.getMessage()
        );
        problem.setTitle("Task not found");
        problem.setInstance(
                java.net.URI.create(request.getRequestURI())
        );
        return problem;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail statusNotValid(HttpMessageNotReadableException e, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
        );
        problem.setTitle("Invalid status");
        problem.setInstance(
                java.net.URI.create(request.getRequestURI())
        );
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail argumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {
        String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                errorMessage
        );
        problem.setTitle("Validation failed");
        problem.setInstance(
                java.net.URI.create(request.getRequestURI())
        );
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail internalServerError(Exception e, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage()
        );
        problem.setTitle("Unexpected server error");
        problem.setInstance(
                java.net.URI.create(request.getRequestURI())
        );
        logger.error("Unexpected server error", e);
        return problem;
    }
}
