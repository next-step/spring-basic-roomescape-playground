package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
