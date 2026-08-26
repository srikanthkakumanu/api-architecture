package com.example.clinic.shared.internal;

import com.example.clinic.shared.api.BusinessRuleException;
import com.example.clinic.shared.api.NotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    ProblemDetail notFound(NotFoundException exception) {
        return problem(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(BusinessRuleException.class)
    ProblemDetail conflict(BusinessRuleException exception) {
        return problem(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    ProblemDetail badRequest(Exception exception) {
        return problem(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    private ProblemDetail problem(HttpStatus status, String detail) {
        var problem = ProblemDetail.forStatus(status);
        problem.setDetail(detail);
        return problem;
    }
}
