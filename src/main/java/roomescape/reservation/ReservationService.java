package roomescape.reservation;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.web.server.ResponseStatusException;
import roomescape.auth.domain.LoginMember;

@Service
public class ReservationService {
    private ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        if (!reservationRequest.getName().equals(loginMember.name()) && !loginMember.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 이름으로만 예약할 수 있습니다.");
        }

        Reservation reservation = reservationDao.save(reservationRequest);

        return new ReservationResponse(reservation.getId(), reservationRequest.getName(),
                reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    public void deleteById(Long id, LoginMember loginMember) {
        Reservation reservation = reservationDao.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "예약을 찾을 수 없습니다."));

        if (!loginMember.isAdmin() && !reservation.getName().equals(loginMember.name())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "자신의 예약만 삭제할 수 있습니다.");
        }

        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }
}
