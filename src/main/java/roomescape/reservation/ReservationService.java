package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.member.LoginMember;
import roomescape.member.MemberDao;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final MemberDao memberDao;

    public ReservationService(ReservationDao reservationDao, MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        String name = reservationRequest.name();

        if (name == null || name.isEmpty()) {
            name = loginMember.name();
        }

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("이름이 비어있습니다.");
        }

        ReservationRequest validRequest = new ReservationRequest(name, reservationRequest.date(), reservationRequest.theme(), reservationRequest.time());
        Reservation reservation = reservationDao.save(validRequest);

        return new ReservationResponse(reservation.getId(), name, reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
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
