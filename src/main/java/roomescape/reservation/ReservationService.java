package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.auth.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberDao;
import roomescape.theme.Theme;
import roomescape.theme.ThemeDao;
import roomescape.time.Time;
import roomescape.time.TimeDao;

import java.util.List;

@Service
public class ReservationService {
    private final TimeDao timeDao;
    private final ReservationDao reservationDao;
    private final MemberDao memberDao;
    private final ThemeDao themeDao;

    public ReservationService(TimeDao timeDao, ReservationDao reservationDao, MemberDao memberDao, ThemeDao themeDao) {
        this.timeDao = timeDao;
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
        this.themeDao = themeDao;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        if (reservationRequest.getName() == null) {
            reservationRequest = new ReservationRequest(loginMember.name(),
                    reservationRequest.getDate(), reservationRequest.getTheme(), reservationRequest.getTime());
        }

        Member memberById = memberDao.findMemberById(loginMember.id());
        Time time = timeDao.findById(reservationRequest.getTime());
        Theme theme = themeDao.findById(reservationRequest.getTheme());

        Reservation reservation = reservationDao.save(new Reservation(reservationRequest.getName(), reservationRequest.getDate(), time, theme, memberById));
        return new ReservationResponse(reservation.getId(), reservationRequest.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(),
                        it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    public List<MyReservationsResponse> findMyReservations(LoginMember loginMember) {
        List<Reservation> reservations = reservationDao.findByMemberId(loginMember.id());
        return reservations.stream()
                .map(it -> new MyReservationsResponse(it.getId(), it.getTheme().getName(),
                        it.getDate(), it.getTime().getValue(), "예약"))
                .toList();
    }
}
