package br.julio.mariano.hackathon_santo_digital.infrastructure.exception;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.openapitools.model.ApiError;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private ApiError standardError(Exception ex){
        return new ApiError()
            .timestamp(OffsetDateTime.now())
            .message(ex.getMessage());
    }

    @Override
    @Nullable
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiError responseError = standardError(ex);

        List<String> fieldErrors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(field -> field.getField() + ": " + field.getDefaultMessage())
            .collect(Collectors.toList());

        responseError.setFieldErrors(fieldErrors);
        responseError.setMessage("Input validation error!");
    
        log.error("Method Argument Not Valid Exception ::: {}", responseError);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex){
        ApiError responseError = standardError(ex);
        log.error("Entity Not Found Exception ::: {}", responseError);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFound(UsernameNotFoundException ex){
        ApiError responseError = standardError(ex);
        log.error("Entity Not Found Exception ::: {}", responseError);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentials(BadCredentialsException ex){
        ApiError responseError = standardError(ex);
        log.error("Entity Not Found Exception ::: {}", responseError);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex){
        ApiError responseError = standardError(ex);
        log.error("Data Integrity Violation Exception ::: {}", responseError);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex){
        ApiError responseError = standardError(ex);
        responseError.setMessage("Constraint violation error!");
        responseError.setFieldErrors(ex.getConstraintViolations().stream().map(field -> field.getMessage()).toList());
        log.error("Constraint Violation Exception ::: {}", responseError);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

}
