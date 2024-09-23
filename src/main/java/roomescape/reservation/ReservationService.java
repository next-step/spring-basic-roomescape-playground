package roomescape.reservation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import roomescape.reservation.controller.dto.ReservationRequest;
import roomescape.reservation.controller.dto.ReservationResponse;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    @Transactional
    public ReservationResponse save(ReservationRequest reservationRequest) {
        Reservation reservation = reservationDao.save(reservationRequest);

        return new ReservationResponse(reservation.getId(), reservationRequest.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    @Transactional
    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }
}
