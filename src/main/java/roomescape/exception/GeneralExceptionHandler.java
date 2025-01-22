package roomescape.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler({MemberNotFoundException.class, JwtValidationException.class, JwtProviderException.class})
    public ResponseEntity<ErrorResponse> handleMemberNotFound(Exception e) {
        ErrorResponse authenticationErrorResponse = new ErrorResponse(e.getMessage(), "authentication_error");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authenticationErrorResponse);
    }

    @ExceptionHandler({TimeNotFoundException.class, ThemeNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleTimeNotFound(Exception e) {
        ErrorResponse notFoundErrorResponse = new ErrorResponse(e.getMessage(), "not_found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundErrorResponse);
    }

    @ExceptionHandler(DuplicateReservationException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatedReservation(DuplicateReservationException e) {
        ErrorResponse duplicationErrorResponse = new ErrorResponse(e.getMessage(), "duplicate_reservation");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(duplicationErrorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception e) {
        log.error("Exception [Err_Location] : {}", e.getStackTrace()[0], e);
        ErrorResponse errorResponse = new ErrorResponse("잠깐 문제가 생겼어요. 다음에 다시 시도해주세요.", "internal_server_error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
