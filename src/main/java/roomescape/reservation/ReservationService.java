package roomescape.reservation;

import org.springframework.stereotype.Service;
import java.util.List;
import roomescape.member.LoginMember;

@Service
public class ReservationService {
    private ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        String reservationName;
        if (reservationRequest.getName() != null && !reservationRequest.getName().isBlank()) {
            reservationName = reservationRequest.getName();
        } else {
            if (loginMember == null) {
                throw new IllegalArgumentException("로그인 정보가 없거나 예약자 이름이 비어있습니다.");
            }
            reservationName = loginMember.getName();
        }
        Reservation reservation = reservationDao.save(reservationRequest, reservationName);

        return new ReservationResponse(reservation.getId(), reservationName, reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    public void deleteById(Long id, LoginMember loginMember) {
        if (loginMember == null) {
            throw new IllegalArgumentException("로그인 정보가 없습니다.");
        }

        Reservation reservation = reservationDao.findById(id);

        if (!"ADMIN".equals(loginMember.getRole()) && !reservation.getName().equals(loginMember.getName())) {
            throw new IllegalArgumentException("본인의 예약만 삭제할 수 있습니다.");
        }

        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }
}
