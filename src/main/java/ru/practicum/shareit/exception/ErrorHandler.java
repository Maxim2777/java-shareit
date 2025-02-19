package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ErrorHandler {

    // Обработка ошибок валидации (@NotBlank, @NotNull, @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return createErrorResponse(HttpStatus.BAD_REQUEST, errors);
    }

    // Обработка ошибки отсутствующего заголовка (400 Bad Request)
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Map<String, Object>> handleMissingHeader(MissingRequestHeaderException e) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, "Missing required header: " + e.getHeaderName());
    }

    // Обработка ошибки некорректного `state` в бронированиях (400 Bad Request)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException e) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    // Обработка 403 Forbidden (если не владелец вещи пытается подтвердить бронирование)
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Map<String, Object>> handleForbiddenException(ForbiddenException e) {
        return createErrorResponse(HttpStatus.FORBIDDEN, e.getMessage());
    }

    // Обработка 409 CONFLICT (например, дублирующийся email)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleConflictException(IllegalStateException e) {
        return createErrorResponse(HttpStatus.CONFLICT, e.getMessage());
    }

    // Обработка 404 NOT FOUND (например, если пользователя или вещи нет)
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NoSuchElementException e) {
        return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    // Обработка 500 INTERNAL SERVER ERROR (непредвиденные ошибки)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleOtherExceptions(Exception e) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + e.getMessage());
    }

    // Формируем JSON-ответ
    private ResponseEntity<Map<String, Object>> createErrorResponse(HttpStatus status, Object message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", status.value());
        errorResponse.put("error", status.getReasonPhrase());
        errorResponse.put("message", message);
        errorResponse.put("timestamp", System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, status);
    }
}