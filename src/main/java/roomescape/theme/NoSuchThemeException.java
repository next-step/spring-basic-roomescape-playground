package roomescape.theme;

import org.springframework.http.HttpStatus;
import roomescape.exception.BusinessException;

public class NoSuchThemeException extends BusinessException {
    public NoSuchThemeException() {
        super("No such Theme", HttpStatus.NOT_FOUND);
    }
}
