package inxj.newsfeed.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<Map<String, Object>> handleCustomException(CustomException ex, HttpServletRequest request) {
    ErrorCode errorCode = ex.getErrorCode();

    return ResponseEntity
        .status(errorCode.getHttpStatus())
        .body(Map.of(
            "timestamp", LocalDateTime.now(),
            "status", errorCode.getHttpStatus().value(),
            "error", errorCode.getHttpStatus().name(),
            "code", errorCode.getErrorCode(),
            "message", errorCode.getMessage(),
            "path", request.getRequestURI()
        ));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex,
      HttpServletRequest request) {
    List<Map<String, String>> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
        .map(error -> Map.of(
            "field", error.getField(),
            "message", Objects.requireNonNull(error.getDefaultMessage())
        )).collect(Collectors.toList());

    return ResponseEntity
        .badRequest()
        .body(Map.of(
            "timestamp", LocalDateTime.now(),
            "status", 400,
            "error", "BAD_REQUEST",
            "code", "C001",
            "message", "잘못된 입력값입니다",
            "path", request.getRequestURI(),
            "fieldErrors", fieldErrors
        ));
  }


  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String,Object>> handleConstraintViolation(ConstraintViolationException ex,
      HttpServletRequest request) {

    return ResponseEntity
        .badRequest()
        .body(Map.of(
            "timestamp", LocalDateTime.now(),
            "status", 400,
            "error", "BAD_REQUEST",
            "code", "400",
            "message", ex.getMessage(),
            "path", request.getRequestURI()
        ));
  }

}
