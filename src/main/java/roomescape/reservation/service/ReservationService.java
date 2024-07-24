package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import roomescape.reservation.controller.dto.ReservationRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDao;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public ReservationResponse save(String memberName,
                                    ReservationRequest reservationRequest) {
        Reservation reservation = reservationDao.save(requestToReservation(memberName, reservationRequest));
        return reservationToResponse(reservation);
    }

    private ReservationResponse reservationToResponse(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    private ReservationRequest requestToReservation(String memberName, ReservationRequest reservationRequest) {
        return new ReservationRequest(memberName, reservationRequest.date(), reservationRequest.theme(), reservationRequest.time());
    }

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(this::reservationToResponse)
                .toList();
    }
}
