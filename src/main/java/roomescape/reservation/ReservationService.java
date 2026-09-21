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

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember member) {
        ReservationRequest reservationToSave = withName(reservationRequest, member);
        return save(reservationToSave);
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Reservation reservation = reservationDao.save(reservationRequest);
        return new ReservationResponse(reservation.getId(),
                reservation.getName(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue());
    }

    public ReservationRequest withName(ReservationRequest reservationRequest, LoginMember member) {
        if (reservationRequest.getName() == null || reservationRequest.getName().isBlank()) {
            return new ReservationRequest(member.name(), reservationRequest.getDate(), reservationRequest.getTheme(), reservationRequest.getTime());
        }
        return reservationRequest;
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
