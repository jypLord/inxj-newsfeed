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

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ✅ BaseException 처리
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Map<String, Object>> handleBaseException(BaseException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getErrorCode(), request.getRequestURI());
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

        return ResponseEntity.badRequest().body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", 400,
                "error", "BAD_REQUEST",
                "code", "C001",
                "message", "잘못된 입력값입니다",
                "path", request.getRequestURI(),
                "fieldErrors", fieldErrors
        ));
    }

    // ✅ 제약조건 위반
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        return buildBasicError(400, "BAD_REQUEST", "400", ex.getMessage(), request.getRequestURI());
    }

    // ✅ 파라미터 타입 불일치
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(Exception e, HttpServletRequest request) {
        log.error("MethodArgumentTypeMismatchException : {}", e.getMessage());
        return buildBasicError(400, "BAD_REQUEST", "C002", "요청 파라미터 타입이 잘못되었습니다.", request.getRequestURI());
    }

    // ✅ Spring 6 이상 유효성 실패
    @ExceptionHandler(MethodValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptionSpring6(Exception e, HttpServletRequest request) {
        log.error("HandlerMethodValidationException : {}", e.getMessage());
        return buildBasicError(400, "BAD_REQUEST", "C003", "유효성 검사 실패", request.getRequestURI());
    }

    // ✅ 요청 헤더 누락
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Map<String, Object>> handleMissingHeader(Exception e, HttpServletRequest request) {
        log.error("MissingRequestHeaderException : {}", e.getMessage());
        return buildBasicError(400, "BAD_REQUEST", "C004", "필수 요청 헤더가 없습니다", request.getRequestURI());
    }

    // ✅ 요청 파라미터 누락
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingParameter(Exception e, HttpServletRequest request) {
        log.error("MissingServletRequestParameterException : {}", e.getMessage());
        return buildBasicError(400, "BAD_REQUEST", "C005", "필수 요청 파라미터가 없습니다", request.getRequestURI());
    }

    // ✅ JSON 역직렬화 실패
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleUnreadableMessage(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.error("HttpMessageNotReadableException : {}", e.getMessage());
        return buildBasicError(400, "BAD_REQUEST", "C006", "요청 본문이 올바르지 않습니다", request.getRequestURI());
    }

    // ✅ 존재하지 않는 URI
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoHandler(NoHandlerFoundException e, HttpServletRequest request) {
        log.error("NoHandlerFoundException : {}", e.getMessage());
        return buildBasicError(404, "NOT_FOUND", "C007", "존재하지 않는 URI입니다", request.getRequestURI());
    }

    // ✅ 지원하지 않는 HTTP 메서드
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleUnsupportedMethod(Exception e, HttpServletRequest request) {
        log.error("HttpRequestMethodNotSupportedException : {}", e.getMessage());
        return buildBasicError(405, "METHOD_NOT_ALLOWED", "C008", "지원하지 않는 HTTP 메서드입니다", request.getRequestURI());
    }

    // ✅ 알 수 없는 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleUnknownException(Exception e, HttpServletRequest request) {
        log.error("Exception : {} {}", e.getClass(), e.getMessage());
        return buildBasicError(500, "INTERNAL_SERVER_ERROR", "C999", "서버 내부 오류가 발생했습니다", request.getRequestURI());
    }

    // ✅ 공통 응답 생성 메서드 (ErrorCode 기반)
    private ResponseEntity<Map<String, Object>> buildErrorResponse(ErrorCode errorCode, String path) {
        return ResponseEntity.status(errorCode.getHttpStatus()).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", errorCode.getHttpStatus().value(),
                "error", errorCode.getHttpStatus().name(),
                "code", errorCode.getErrorCode(),
                "message", errorCode.getMessage(),
                "path", path
        ));
    }

    // ✅ 공통 응답 생성 메서드 (직접 정의)
    private ResponseEntity<Map<String, Object>> buildBasicError(int status, String error, String code, String message, String path) {
        return ResponseEntity.status(status).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", status,
                "error", error,
                "code", code,
                "message", message,
                "path", path
        ));
    }
}
