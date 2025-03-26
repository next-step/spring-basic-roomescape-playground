package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import roomescape.auth.dto.LoginMember;
import roomescape.exception.BadRequestException;
import roomescape.exception.ExceptionMessage;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.request.ReservationRequest;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.Time;
import roomescape.time.repository.TimeRepository;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository, TimeRepository timeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        reservationRequest = updateRequestIfNameIsInvalid(reservationRequest, loginMember);
        Time time = findTime(reservationRequest.getTime());
        Theme theme = findTheme(reservationRequest.getTheme());
        Reservation reservation = reservationRequest.toReservation(time, theme);
        Reservation reservationWithId = reservationRepository.save(reservation);
        return new ReservationResponse(reservationWithId);
    }

    private ReservationRequest updateRequestIfNameIsInvalid(ReservationRequest reservationRequest, LoginMember loginMember) {
        if (reservationRequest.isInvalidName()) {
            reservationRequest = createReservationRequestWithName(reservationRequest, loginMember);
        }
        return reservationRequest;
    }

    private ReservationRequest createReservationRequestWithName(ReservationRequest reservationRequest, LoginMember loginMember) {
        String name = loginMember.name();
        return reservationRequest.createWith(name);
    }

    private Time findTime(long timeId) {
        return timeRepository.findById(timeId)
                .orElseThrow(() -> new BadRequestException(ExceptionMessage.INVALID_TIME.getMessage()));
    }

    private Theme findTheme(long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new BadRequestException(ExceptionMessage.INVALID_THEME.getMessage()));
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::new)
                .toList();
    }
}
