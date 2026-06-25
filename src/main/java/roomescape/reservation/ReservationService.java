package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.auth.LoginMember;
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

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        Member member = findReservationMember(reservationRequest, loginMember);
        Reservation reservation = reservationDao.save(reservationRequest, member.getName());

        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue()
        );
    }

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(it -> new ReservationResponse(
                        it.getId(),
                        it.getName(),
                        it.getTheme().getName(),
                        it.getDate(),
                        it.getTime().getValue()
                ))
                .toList();
    }

    private Member findReservationMember(ReservationRequest reservationRequest, LoginMember loginMember) {
        if (hasName(reservationRequest)) {
            return memberDao.findByName(reservationRequest.getName());
        }

        if (loginMember == null) {
            throw new IllegalArgumentException("로그인 정보가 필요합니다.");
        }

        return memberDao.findById(loginMember.getId());
    }

    private boolean hasName(ReservationRequest reservationRequest) {
        return reservationRequest.getName() != null
                && !reservationRequest.getName().isBlank();
    }
}
