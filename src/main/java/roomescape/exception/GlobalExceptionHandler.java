package roomescape.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleRoomEscapeException(ApplicationException e) {
        e.printStackTrace();

        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(new ErrorResponse(errorCode.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleRuntimeException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().build();
    }
}
