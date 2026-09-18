package roomescape.reservation;

import org.springframework.stereotype.Service;

import java.util.List;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Service
public class ReservationService {

    private ReservationDao reservationDao;
    private MemberDao memberDao;

    public ReservationService(ReservationDao reservationDao, MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
    }

    public ReservationResponse save(ReservationRequest reservationRequest,
        LoginMember loginMember) {
        Member member;
        if (reservationRequest.getName() != null) {
            member = memberDao.findByName(reservationRequest.getName());
        } else {
            member = memberDao.findByName(loginMember.getName());
        }
        Reservation reservation = reservationDao.save(reservationRequest, member.getName());

        return new ReservationResponse(reservation.getId(), reservation.getName(),
            reservation.getTheme().getName(), reservation.getDate(),
            reservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
            .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(),
                it.getDate(), it.getTime().getValue()))
            .toList();
    }
}
