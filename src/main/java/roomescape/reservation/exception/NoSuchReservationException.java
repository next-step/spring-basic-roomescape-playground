package roomescape.reservation.exception;

import org.springframework.http.HttpStatus;
import roomescape.exception.BusinessException;

public class NoSuchReservationException extends BusinessException {
    public NoSuchReservationException() {
        super("No such Reservation", HttpStatus.NOT_FOUND);
    }
}
