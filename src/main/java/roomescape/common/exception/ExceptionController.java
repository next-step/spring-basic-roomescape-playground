package roomescape.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(RoomScapeException.class)
    public ResponseEntity<CustomExceptionResponse> handleRoomScapeException(final RoomScapeException roomScapeException) {
        final HttpStatus status = HttpStatus.valueOf(roomScapeException.getStatusCode());
        final CustomExceptionResponse errorResponse = new CustomExceptionResponse(
                roomScapeException.getStatusCode(), roomScapeException.getMessage()
        );
        return ResponseEntity.status(status)
                .body(errorResponse);
    }
}
