package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.auth.LoginMember;
import roomescape.exception.ApplicationException;
import roomescape.member.MemberDao;
import roomescape.member.MemberErrorCode;

import java.util.List;

@Service
public class ReservationService {
    private ReservationDao reservationDao;
    private MemberDao memberDao;

    public ReservationService(ReservationDao reservationDao, MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        String name = resolveName(reservationRequest, loginMember);

        Reservation reservation = reservationDao.save(reservationRequest, name);

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

    private String resolveName(ReservationRequest reservationRequest, LoginMember loginMember) {
        if (reservationRequest.getName() != null) {
            memberDao.findByName(reservationRequest.getName())
                    .orElseThrow(() -> new ApplicationException(MemberErrorCode.MEMBER_NOT_FOUND));
            return reservationRequest.getName();
        }

        return loginMember.name();
    }
}
