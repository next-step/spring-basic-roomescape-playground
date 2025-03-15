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
        reservationRequest = updateRequestIfNameIsInvalid(reservationRequest, loginMember);
        Reservation reservation = reservationDao.save(reservationRequest);
        return new ReservationResponse(reservation);
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
