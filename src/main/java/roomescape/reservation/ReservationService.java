package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.member.LoginMember;

import java.util.List;

@Service
public class ReservationService {
    private ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        if (reservationRequest.getName() == null) {
            reservationRequest = new ReservationRequest(loginMember.name(),
                    reservationRequest.getDate(), reservationRequest.getTheme(), reservationRequest.getTime());
        }
        System.out.println("reservationRequest = " + loginMember.name()+" "+reservationRequest.getDate());
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
