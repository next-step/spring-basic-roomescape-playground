package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import roomescape.auth.controller.LoginMember;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.request.ReservationRequest;
import roomescape.reservation.dto.response.ReservationResponse;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        if (reservationRequest.isInvalidName()) {
            reservationRequest = getReservationRequestWithName(reservationRequest, loginMember);
        }
        Reservation reservation = reservationDao.save(reservationRequest);
        return new ReservationResponse(reservation.getId(), reservationRequest.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    private ReservationRequest getReservationRequestWithName(ReservationRequest reservationRequest, LoginMember loginMember) {
        String name = loginMember.name();
        reservationRequest = reservationRequest.createWith(name);
        return reservationRequest;
    }

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }
}
