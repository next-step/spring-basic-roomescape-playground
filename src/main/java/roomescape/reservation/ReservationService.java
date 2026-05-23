package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberDao;

import java.util.List;

@Service
public class ReservationService {
    private final MemberDao memberDao;
    private final ReservationDao reservationDao;

    public ReservationService(MemberDao memberDao, ReservationDao reservationDao) {
        this.memberDao = memberDao;
        this.reservationDao = reservationDao;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        Member member = findMember(reservationRequest, loginMember);
        Reservation reservation = reservationDao.save(reservationRequest, member);

        return new ReservationResponse(reservation.getId(), member.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    private Member findMember(ReservationRequest reservationRequest, LoginMember loginMember) {
        if (reservationRequest.name() != null && !reservationRequest.name().isBlank()) {
            return memberDao.findByName(reservationRequest.name());
        }
        return memberDao.findById(loginMember.id());
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
