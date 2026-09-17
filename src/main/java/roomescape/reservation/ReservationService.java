package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberDao;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final MemberDao memberDao;

    public ReservationService(
            ReservationDao reservationDao,
            MemberDao memberDao
    ) {
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
    }

    public ReservationResponse save(
            ReservationRequest reservationRequest,
            LoginMember loginMember
    ) {
        Member member = findReservationMember(
                reservationRequest,
                loginMember
        );

        Reservation reservation = reservationDao.save(
                reservationRequest,
                member.getName()
        );

        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue()
        );
    }

    private Member findReservationMember(
            ReservationRequest reservationRequest,
            LoginMember loginMember
    ) {
        String name = reservationRequest.getName();

        if (name == null || name.isBlank()) {
            return memberDao.findById(loginMember.getId());
        }

        return memberDao.findByName(name);
    }

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(reservation -> new ReservationResponse(
                        reservation.getId(),
                        reservation.getName(),
                        reservation.getTheme().getName(),
                        reservation.getDate(),
                        reservation.getTime().getValue()
                ))
                .toList();
    }
}
