package roomescape.reservation;

import org.springframework.stereotype.Component;
import roomescape.exception.ErrorMessage;
import roomescape.exception.InvalidDataException;
import roomescape.waiting.WaitingRepository;

@Component
public class ReservationValidator {
    private final ReservationRepository reservationRepository;
    private final WaitingRepository waitingRepository;

    public ReservationValidator(ReservationRepository reservationRepository, WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.waitingRepository = waitingRepository;
    }

    public void validateReservationCreation(Long memberId, String date, Long timeId, Long themeId) {
        validateMemberHasNoReservation(memberId, date, timeId, themeId);

        if (reservationRepository.existsByDateAndThemeIdAndTimeId(date, themeId, timeId)) {
            throw new InvalidDataException(ErrorMessage.RESERVATION_TIME_ALREADY_BOOKED.getMessage());
        }
    }

    public void validateWaitingCreation(Long memberId, String date, Long timeId, Long themeId) {
        validateMemberHasNoReservation(memberId, date, timeId, themeId);

        if (waitingRepository.existsByMemberIdAndDateAndTimeIdAndThemeId(memberId, date, timeId, themeId)) {
            throw new InvalidDataException(ErrorMessage.WAITING_ALREADY_EXISTS.getMessage());
        }
    }

    private void validateMemberHasNoReservation(Long memberId, String date, Long timeId, Long themeId) {
        if (reservationRepository.existsByMemberIdAndDateAndThemeIdAndTimeId(memberId, date, themeId, timeId)) {
            throw new InvalidDataException(ErrorMessage.RESERVATION_ALREADY_EXISTS.getMessage());
        }
    }
}
