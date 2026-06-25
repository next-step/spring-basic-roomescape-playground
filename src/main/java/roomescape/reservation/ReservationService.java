package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.member.DTO.MemberResponse;
import roomescape.reservation.DTO.ReservationRequest;
import roomescape.reservation.DTO.ReservationResponse;

import java.util.List;

@Service
public class ReservationService {
    private ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, MemberResponse member) {
        String reservatorName = reservationRequest.getName() != null ? reservationRequest.getName() : member.getName();

        ReservationRequest newReservation = new ReservationRequest(reservatorName, reservationRequest.getDate(), reservationRequest.getTime(), reservationRequest.getTheme());
        Reservation reservation = reservationDao.save(newReservation);

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
