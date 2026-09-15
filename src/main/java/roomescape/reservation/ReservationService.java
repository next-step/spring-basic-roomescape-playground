package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.member.Member;
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

    public ReservationResponse save(ReservationRequest reservationRequest, Long loginMemberId) {
        Member member = resolveReservationMember(reservationRequest.getName(), loginMemberId);

        Reservation reservation = reservationDao.save(reservationRequest, member.getName());

        return ReservationResponse.from(reservation);
    }

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    private Member resolveReservationMember(String name, Long loginMemberId) {
        if (name != null) {
            return memberDao.findByName(name);
        }

        return memberDao.findById(loginMemberId);
    }
}
