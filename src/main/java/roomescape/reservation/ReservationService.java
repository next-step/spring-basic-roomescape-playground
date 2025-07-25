package roomescape.reservation;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.Member;
import roomescape.member.MemberService;
import roomescape.theme.Theme;
import roomescape.theme.ThemeDao;
import roomescape.time.Time;
import roomescape.time.TimeDao;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final MemberService memberService;
    private final TimeDao timeDao;
    private final ThemeDao themeDao;

    public ReservationService(ReservationDao reservationDao, MemberService memberService, TimeDao timeDao,
                              ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.memberService = memberService;
        this.timeDao = timeDao;
        this.themeDao = themeDao;
    }

    @Transactional
    public ReservationResponse save(ReservationRequest reservationRequest, Member loginMember) {
        Member member = getMemberFromRequest(reservationRequest, loginMember);
        Time time = timeDao.findById(reservationRequest.time());
        Theme theme = themeDao.findById(reservationRequest.theme());

        Reservation reservation = new Reservation(member.getName(), reservationRequest.date(), time, theme);
        Reservation savedReservation = reservationDao.save(reservation);

        return new ReservationResponse(
                savedReservation.getId(),
                savedReservation.getName(),
                savedReservation.getTheme().getName(),
                savedReservation.getDate(),
                savedReservation.getTime().getValue()
        );
    }

    private Member getMemberFromRequest(ReservationRequest reservationRequest, Member loginMember) {
        if (reservationRequest.name() != null) {
            return memberService.findByName(reservationRequest.name());
        }
        return loginMember;
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(),
                        it.getTime().getValue()))
                .toList();
    }

    @Transactional
    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }
}
