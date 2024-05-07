package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.member.MemberResponse;

import java.util.List;

@Service
public class ReservationService {
    private ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public ReservationResponse save(MemberResponse memberResponse, ReservationRequest reservationRequest) {
        Reservation reservation = reservationDao.save( reservationRequest.getName() == null? new ReservationRequest(memberResponse.getName(),reservationRequest.getDate(),reservationRequest.getTheme(),reservationRequest.getTime()) : reservationRequest);

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
