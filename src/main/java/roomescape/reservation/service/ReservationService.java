package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import roomescape.member.entity.Member;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dao.ReservationDao;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, Member member) {

        String reservationName;
        if (reservationRequest.getName() == null) {
            reservationName = member.getName();
        } else {
            reservationName = reservationRequest.getName();
        }

        Reservation reservation = reservationDao.save(reservationRequest, reservationName);

        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue()
        );
    }

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(),
                                    it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }
}
