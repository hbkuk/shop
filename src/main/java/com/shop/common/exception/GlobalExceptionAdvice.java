package com.shop.common.exception;

import com.shop.common.exception.dto.ErrorResponse;
import com.shop.common.exception.dto.ValidationErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

import static com.shop.common.exception.ErrorType.UNHANDLED_EXCEPTION;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> badRequestExceptionHandler(final BadRequestException e) {
        log.warn("Bad Request Exception", e);
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> unauthorizedExceptionHandler(final UnauthorizedException e) {
        log.warn("Unauthorized Exception", e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> forbiddenExceptionHandler(final ForbiddenException e) {
        log.warn("Forbidden Exception", e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            errors.put(((FieldError) error).getField(), error.getDefaultMessage());
        });
        return ResponseEntity.unprocessableEntity().body(new ValidationErrorResponse(ErrorType.UNPROCESSABLE_ENTITY.getCode(), errors));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ValidationErrorResponse> handleBindException(final BindException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            errors.put(((FieldError) error).getField(), error.getDefaultMessage());
        });
        return ResponseEntity.unprocessableEntity().body(new ValidationErrorResponse(ErrorType.UNPROCESSABLE_ENTITY.getCode(), errors));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.error("NoHandlerFoundException", e);
        return ResponseEntity.badRequest().body(new ErrorResponse(ErrorType.INVALID_PATH.getCode(),
                ErrorType.INVALID_PATH.getMessage()));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> unHandledExceptionHandler(final Exception e) {
        log.error("Not Expected Exception is Occurred", e);
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse(UNHANDLED_EXCEPTION.getCode(), UNHANDLED_EXCEPTION.getMessage()));
    }

}


