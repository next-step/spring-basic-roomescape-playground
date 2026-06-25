package roomescape.reservation;

import org.springframework.stereotype.Service;

import java.util.List;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Service
public class ReservationService {
    private ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, Member member) {
        if (reservationRequest.getName() == null || reservationRequest.getName().isBlank()) {
            if (member == null || member.getName().isBlank()) {
                throw new IllegalArgumentException("예약자 이름 또는 로그인 정보가 필요합니다.");
            }
            reservationRequest.setName(member.getName());
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
