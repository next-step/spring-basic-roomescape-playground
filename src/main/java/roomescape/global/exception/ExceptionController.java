package roomescape.global.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import roomescape.member.exception.MemberNotFoundException;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> handleMemberNotFoundException(Exception e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(400, e.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
