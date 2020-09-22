package com.techgentsia.ecomexample.customerms.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import static com.techgentsia.ecomexample.customerms.constants.MessageConstants.*;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Autowired
    ResourceBundleMessageSource messageSource;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex,
                                                             WebRequest request,
                                                             @RequestHeader("Accept-Language") Locale local) {
        ErrorDetails errorDetails
                = new ErrorDetails(new Date(),
                messageSource.getMessage(ex.getMessage(),null, local) + ex.getId(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException ex, @RequestHeader("Accept-Language") Locale local) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), messageSource.getMessage(BAD_CREDENTIALS_ERROR,null, local));
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Locale local = request.getLocale();
        Map<String, String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .collect(Collectors.toMap(error -> ((FieldError) error).getField(),
                        error -> messageSource.getMessage(error.getDefaultMessage(),
                                null, local)));
        String errorMessage;
        Object details;
        if (errors.size() > 0) {
            errorMessage = messageSource.getMessage(VALIDATION_FAILED_ERROR, new String[]{String.valueOf(errors.size())}, local);
            details = errors;
        } else {
            errorMessage = ex.getLocalizedMessage();
            details = request.getDescription(false);
        }
        ErrorDetails errorDetails = new ErrorDetails(new Date(), errorMessage, details);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleOtherException(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
