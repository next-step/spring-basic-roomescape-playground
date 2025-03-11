package roomescape.reservation;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final MemberDao memberDao;

    public ReservationService(ReservationDao reservationDao, MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Reservation reservation = reservationDao.save(reservationRequest);

        return new ReservationResponse(reservation.getId(), reservationRequest.getName(),
                reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        Member foundMember = memberDao.findById(loginMember.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Reservation reservation = reservationDao.save(reservationRequest.getDate(), foundMember.getName(),
                reservationRequest.getTheme(), reservationRequest.getTime());
        return new ReservationResponse(reservation.getId(), reservation.getName(),
                reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(),
                        it.getTime().getValue()))
                .toList();
    }
}
