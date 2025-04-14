package roomescape.global.exception;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleRuntimeException(Exception exception) {
        exception.printStackTrace();
        return ResponseEntity.internalServerError()
                .body("서버 에러 관리자에게 문의해주세요.");
    }

    @ExceptionHandler(RoomescapeException.class)
    public ResponseEntity handleRoomescapeException(RoomescapeException exception) {
        exception.printStackTrace();

        return ResponseEntity.status(exception.getStatus())
                .body(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleInvalidFormatException(MethodArgumentNotValidException
                                                                                 exception) {
        List<String> errors = new ArrayList<>();
        exception.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errors.add("[" + fieldError.getField() + "] " + fieldError.getDefaultMessage());
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleParsing(HttpMessageNotReadableException exception) {
        return ResponseEntity.badRequest().body("요청 본문 형식이 잘못되었습니다.");
    }
}
