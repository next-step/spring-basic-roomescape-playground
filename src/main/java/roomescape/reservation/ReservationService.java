package roomescape.reservation;

import org.springframework.stereotype.Service;

import java.util.List;
import roomescape.member.dto.LoginMember;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;

@Service
public class ReservationService {
    private ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public ReservationResponse save(ReservationRequest reservationRequest,
                                    LoginMember loginMember) {
        if (reservationRequest.getName() == null) {
            ReservationRequest newRequest = new ReservationRequest(
                    loginMember.name(),
                    reservationRequest.getDate(),
                    reservationRequest.getTime(),
                    reservationRequest.getTheme()
            );
            reservationRequest = newRequest;
        }
        Reservation reservation = reservationDao.save(reservationRequest);

        return new ReservationResponse(reservation.getId(), reservationRequest.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
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
