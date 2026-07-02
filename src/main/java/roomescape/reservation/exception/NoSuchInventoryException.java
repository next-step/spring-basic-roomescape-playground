package roomescape.reservation.exception;

import org.springframework.http.HttpStatus;
import roomescape.exception.BusinessException;

public class NoSuchInventoryException extends BusinessException {
    public NoSuchInventoryException() {
        super("예약 가능한 건이 없습니다.", HttpStatus.NOT_FOUND);
    }
}