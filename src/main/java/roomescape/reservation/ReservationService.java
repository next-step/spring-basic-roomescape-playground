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
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;


    public ReservationService(ReservationDao reservationDao, MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.reservationDao = reservationDao;
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

        ReservationRequest request = new ReservationRequest(name, reservationRequest.date(), reservationRequest.theme(), reservationRequest.time());

        Reservation saved = reservationDao.save(request);

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
