package roomescape.reservation.exception;

import org.springframework.http.HttpStatus;
import roomescape.exception.BusinessException;

public class AlreadyBookedTimeReservationException extends BusinessException {
    public AlreadyBookedTimeReservationException() {
        super("The selected time slot is already booked.", HttpStatus.CONFLICT);
    }
}
