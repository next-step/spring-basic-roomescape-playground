package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.auth.LoginMember;

import java.util.List;

@Service
public class ReservationService {
    private ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        String name = resolveName(reservationRequest.getName(), loginMember);
        Reservation reservation = reservationDao.save(name, reservationRequest.getDate(), reservationRequest.getTheme(), reservationRequest.getTime());

        return new ReservationResponse(reservation.getId(), name, reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    private String resolveName(String requestName, LoginMember loginMember) {
        if (requestName == null || requestName.isBlank()) {
            return loginMember.getName();
        }
        return requestName;
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
