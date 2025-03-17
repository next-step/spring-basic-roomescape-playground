package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import roomescape.auth.controller.LoginMember;
import roomescape.exception.BadRequestException;
import roomescape.exception.ExceptionMessage;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.request.ReservationRequest;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.theme.dao.ThemeDao;
import roomescape.theme.domain.Theme;
import roomescape.time.dao.TimeDao;
import roomescape.time.domain.Time;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final TimeDao timeDao;
    private final ThemeDao themeDao;

    public ReservationService(ReservationDao reservationDao, TimeDao timeDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.timeDao = timeDao;
        this.themeDao = themeDao;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        reservationRequest = updateRequestIfNameIsInvalid(reservationRequest, loginMember);
        Time time = findTime(reservationRequest.getTime());
        Theme theme = findTheme(reservationRequest.getTheme());
        Reservation reservation = reservationRequest.toReservation(time, theme);
        Reservation reservationWithId = reservationDao.save(reservation);
        return new ReservationResponse(reservationWithId);
    }

    private Time findTime(long timeId) {
        return timeDao.findById(timeId)
                .orElseThrow(() -> new BadRequestException(ExceptionMessage.INVALID_TIME.getMessage()));
    }

    private Theme findTheme(long themeId) {
        return themeDao.findById(themeId)
                .orElseThrow(() -> new BadRequestException(ExceptionMessage.INVALID_THEME.getMessage()));
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

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(ReservationResponse::new)
                .toList();
    }
}
