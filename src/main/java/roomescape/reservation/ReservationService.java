package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.AuthenticationException;
import roomescape.member.LoginMemberInfo;
import roomescape.member.Member;
import roomescape.member.MemberDao;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    private ReservationDao reservationDao;
    private MemberDao memberDao;

    public ReservationService(ReservationDao reservationDao, MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, Optional<LoginMemberInfo> loginMember) {
        Member member = findReservationMember(reservationRequest, loginMember);
        Reservation reservation = reservationDao.save(reservationRequest, member.getName());

        return new ReservationResponse(reservation.getId(), member.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    private Member findReservationMember(ReservationRequest reservationRequest, Optional<LoginMemberInfo> loginMember) {
        if (reservationRequest.getName() != null && !reservationRequest.getName().isBlank()) {
            return memberDao.findByName(reservationRequest.getName());
        }
        if (loginMember.isEmpty()) {
            throw new AuthenticationException();
        }
        return memberDao.findByEmail(loginMember.get().getEmail());
    }

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    public List<ReservationMineResponse> findMine(LoginMemberInfo loginMember) {
        return reservationDao.findByMemberName(loginMember.getName()).stream()
                .map(it -> new ReservationMineResponse(it.getId(), it.getTheme().getName(), it.getDate(), it.getTime().getValue(), "예약"))
                .toList();
    }
}
