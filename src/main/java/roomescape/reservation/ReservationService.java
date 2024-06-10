package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.member.AuthenticationMember;

import java.util.List;

@Service
public class ReservationService {
    private ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public ReservationResponse save(AuthenticationMember authenticationMember, ReservationRequest reservationRequest) {
        if(reservationRequest.getName() != null){
            Reservation reservation = reservationDao.save(reservationRequest);

            return new ReservationResponse(reservation.getId(), reservationRequest.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
        }
        else{
            reservationRequest.setName(authenticationMember.getName());
            Reservation reservation = reservationDao.save(reservationRequest);

            return new ReservationResponse(reservation.getId(), reservationRequest.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
        }
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
