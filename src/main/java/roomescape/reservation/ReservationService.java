package roomescape.reservation;

import org.springframework.stereotype.Service;
import java.util.List;
import roomescape.JwtTokenProvider;
import roomescape.member.Member;
import roomescape.member.MemberService;
import roomescape.theme.Theme;
import roomescape.theme.ThemeDao;
import roomescape.time.Time;
import roomescape.time.TimeDao;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final ThemeDao themeDao;
    private final TimeDao timeDao;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;


    public ReservationService(ReservationDao reservationDao, ThemeDao themeDao, TimeDao timeDao, MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
        this.timeDao = timeDao;
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, String token) {
        String name;
        if (reservationRequest.name() != null) {
            name = reservationRequest.name();
        } else {
            String memberId = jwtTokenProvider.getMemberId(token);
            Member member = memberService.findById(Integer.parseInt(memberId));
            name = member.getName();
        }

        Theme theme = themeDao.findById(Long.parseLong(reservationRequest.theme()));
        Time time = timeDao.findById(Long.parseLong(reservationRequest.time()));

        Reservation reservation = new Reservation(null, name, reservationRequest.date(), time, theme);

        Reservation saved = reservationDao.save(reservation);

        return new ReservationResponse(saved.getId(), saved.getName(), saved.getTheme().getName(), saved.getDate(), saved.getTime().getValue());
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
