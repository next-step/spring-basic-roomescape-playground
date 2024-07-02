package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.LoginMember;

import java.util.List;

@Service
public class ReservationService {
    private ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember member) {

        String name;
        if (reservationRequest.getName() == null) {
            name = member.getName();
        }
        else {
            name = reservationRequest.getName();
        }
        Reservation reservation = reservationDao.save(reservationRequest, name);

        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
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
