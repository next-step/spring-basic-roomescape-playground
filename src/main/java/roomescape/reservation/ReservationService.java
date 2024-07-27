package roomescape.reservation;

import org.springframework.stereotype.Service;

import java.util.List;

import roomescape.global.LoginMember;

@Service
public class ReservationService {
    private ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public ReservationResponse save(LoginMember member, ReservationRequest reservationRequest) {
        if (reservationRequest.getName() == null) {
            Reservation reservation = reservationDao.save(setReservationMemberName(member, reservationRequest));
            return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
        }


        Reservation reservation = reservationDao.save(reservationRequest);
        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    private ReservationRequest setReservationMemberName(LoginMember member, ReservationRequest reservationRequest) {
        return new ReservationRequest(member.getName(), reservationRequest.getDate(), reservationRequest.getTheme(), reservationRequest.getTime());
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
