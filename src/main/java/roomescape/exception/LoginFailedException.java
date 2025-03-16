package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class LoginFailedException extends RuntimeException {
  public LoginFailedException(String message) {
    super(message);
  }
}
