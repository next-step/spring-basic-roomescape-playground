package roomescape.reservation;

import org.springframework.stereotype.Component;
import roomescape.exception.ErrorMessage;
import roomescape.exception.InvalidDataException;
import roomescape.waiting.Waiting;
import roomescape.waiting.WaitingRepository;

import java.util.List;

@Component
public class ReservationValidator {
    private final ReservationRepository reservationRepository;
    private final WaitingRepository waitingRepository;

    public ReservationValidator(ReservationRepository reservationRepository, WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.waitingRepository = waitingRepository;
    }

    public void validateReservationCreation(Long memberId, String date, Long timeId, Long themeId) {
        List<Reservation> reservations = reservationRepository.findByDateAndThemeId(date, themeId);

        boolean hasMemberReservation = reservations.stream()
                .anyMatch(r -> r.getMember() != null
                        && r.getMember().getId().equals(memberId)
                        && r.getTime().getId().equals(timeId));

        if (hasMemberReservation) {
            throw new InvalidDataException(ErrorMessage.RESERVATION_ALREADY_EXISTS.getMessage());
        }

        boolean hasAnyReservation = reservations.stream()
                .anyMatch(r -> r.getTime().getId().equals(timeId));

        if (hasAnyReservation) {
            throw new InvalidDataException(ErrorMessage.RESERVATION_TIME_ALREADY_BOOKED.getMessage());
        }
    }

    public void validateWaitingCreation(Long memberId, String date, Long timeId, Long themeId) {
        List<Reservation> reservations = reservationRepository.findByDateAndThemeId(date, themeId);
        boolean hasReservation = reservations.stream()
                .anyMatch(r -> r.getMember() != null
                        && r.getMember().getId().equals(memberId)
                        && r.getTime().getId().equals(timeId));

        if (hasReservation) {
            throw new InvalidDataException(ErrorMessage.RESERVATION_ALREADY_EXISTS.getMessage());
        }

        List<Waiting> waitings = waitingRepository.findByDateAndTimeIdAndThemeId(date, timeId, themeId);
        boolean hasWaiting = waitings.stream()
                .anyMatch(w -> w.getMember().getId().equals(memberId));

        if (hasWaiting) {
            throw new InvalidDataException(ErrorMessage.WAITING_ALREADY_EXISTS.getMessage());
        }
    }
}
