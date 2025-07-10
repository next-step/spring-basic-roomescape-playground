package roomescape.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResult> handleException(Exception e) {
        ErrorResult errorResult = new ErrorResult("INTERNAL_SERVER_ERROR", "서버 내부 오류 발생");
        return new ResponseEntity<>(errorResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RoomEscapeException.class)
    public ResponseEntity<ErrorResult> handleRoomEscapeException(RoomEscapeException e) {
        ErrorResult errorResult = new ErrorResult(e.getErrorCode().name(), e.getMessage());
        return new ResponseEntity<>(errorResult, e.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResult> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        ErrorResult errorResult = new ErrorResult("INVALID_FORMAT", "형식이 잘못되었습니다");
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }
}
