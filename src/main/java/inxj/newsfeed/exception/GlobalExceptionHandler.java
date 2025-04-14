package inxj.newsfeed.exception;

import inxj.newsfeed.exception.customException.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ✅ BaseException 처리
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Map<String, Object>> handleBaseException(BaseException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getErrorCode(), request.getRequestURI(),null);
    }

    // ✅ @Valid 유효성 검사 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<Map<String, String>> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> Map.of(
                        "field", error.getField(),
                        "message", Optional.ofNullable(error.getDefaultMessage()).orElse("Validation failed")
                ))
                .collect(Collectors.toList());

        return buildErrorResponse(ErrorCode.VALID_ERROR, request.getRequestURI(),fieldErrors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        return buildErrorResponse(ErrorCode.CONSTRAINT_VIOLATION, request.getRequestURI(),null);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        log.error("MethodArgumentTypeMismatchException : {}", ex.getMessage());
        return buildErrorResponse(ErrorCode.TYPE_MISMATCH, request.getRequestURI(),null);
    }

    @ExceptionHandler(MethodValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptionSpring6(MethodValidationException ex, HttpServletRequest request) {
        log.error("MethodValidationException : {}", ex.getMessage());
        return buildErrorResponse(ErrorCode.VALIDATION_FAILED, request.getRequestURI(),null);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Map<String, Object>> handleMissingHeader(MissingRequestHeaderException ex, HttpServletRequest request) {
        log.error("MissingRequestHeaderException : {}", ex.getMessage());
        return buildErrorResponse(ErrorCode.MISSING_HEADER, request.getRequestURI(),null);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingParameter(MissingServletRequestParameterException ex, HttpServletRequest request) {
        log.error("MissingServletRequestParameterException : {}", ex.getMessage());
        return buildErrorResponse(ErrorCode.MISSING_PARAMETER, request.getRequestURI(),null);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleUnreadableMessage(HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.error("HttpMessageNotReadableException : {}", ex.getMessage());
        return buildErrorResponse(ErrorCode.NOT_READABLE_MESSAGE, request.getRequestURI(),null);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoHandler(NoResourceFoundException ex, HttpServletRequest request) {
        log.error("NoHandlerFoundException : {}", ex.getMessage());
        return buildErrorResponse(ErrorCode.NO_HANDLER_FOUND, request.getRequestURI(),null);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleUnsupportedMethod(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        log.error("HttpRequestMethodNotSupportedException : {}", ex.getMessage());
        return buildErrorResponse(ErrorCode.METHOD_NOT_SUPPORTED, request.getRequestURI(),null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleUnknownException(Exception ex, HttpServletRequest request) {
        log.error("Exception : {} {}", ex.getClass(), ex.getMessage());
        return buildErrorResponse(ErrorCode.INTERNAL_ERROR, request.getRequestURI(),null);
    }
    private ResponseEntity<Map<String, Object>> buildErrorResponse(ErrorCode errorCode, String path,List<Map<String, String>> fieldErrors) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", errorCode.getHttpStatus().value());
        body.put("error", errorCode.getHttpStatus().getReasonPhrase());
        body.put("code", errorCode.getErrorCode());
        body.put("message", errorCode.getMessage());
        body.put("path", path);
        body.put("timestamp", LocalDateTime.now());
        if (fieldErrors != null && !fieldErrors.isEmpty()) {
            body.put("fieldErrors", fieldErrors);
        }


        return new ResponseEntity<>(body, errorCode.getHttpStatus());
    }



}
