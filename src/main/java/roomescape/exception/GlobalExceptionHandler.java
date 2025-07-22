package roomescape.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResult> handleException(Exception e) {
        log.warn("예외 발생", e);
        ErrorResult errorResult = new ErrorResult("INTERNAL_SERVER_ERROR", "서버 내부 오류 발생");
        return new ResponseEntity<>(errorResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RoomEscapeException.class)
    public ResponseEntity<ErrorResult> handleRoomEscapeException(RoomEscapeException e) {
        log.warn("예외 발생: {}", e.getMessage());
        ErrorResult errorResult = new ErrorResult(e.getErrorCode().name(), e.getMessage());
        return new ResponseEntity<>(errorResult, e.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResult> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("예외 발생", e);
        ErrorResult errorResult = new ErrorResult("INVALID_FORMAT", "형식이 잘못되었습니다");
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResult> handleDataIntegrityViolation(HttpMessageNotReadableException e) {
        log.warn("예외 발생", e);
        ErrorResult errorResult = new ErrorResult("DUPLICATE_VALUE", "중복된 값이 존재합니다");
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }
}
