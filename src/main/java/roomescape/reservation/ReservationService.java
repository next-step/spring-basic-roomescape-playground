package roomescape.reservation;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberService;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
public class ReservationService {

    private ReservationRepository reservationRepository;
    private TimeRepository timeRepository;
    private ThemeRepository themeRepository;
    private MemberService memberService;

    public ReservationService(ReservationRepository reservationRepository, TimeRepository timeRepository,
                              ThemeRepository themeRepository
        , MemberService memberService) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.memberService = memberService;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Time time = timeRepository.findById(reservationRequest.getTime())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간"));

        Theme theme = themeRepository.findById(reservationRequest.getTheme())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마"));

        Member member = memberService.findByName(reservationRequest.getName());

        Reservation reservation = new Reservation(
            member,
            reservationRequest.getDate(),
            time,
            theme
        );
        reservationRepository.save(reservation);

        return new ReservationResponse(reservation.getId(), reservationRequest.getName(), theme.getName(),
                                       reservation.getDate(),
                                       time.getValue());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
            .map(it -> new ReservationResponse(it.getId(), it.getMemberName(), it.getTheme().getName(), it.getDate(),
                                               it.getTime().getValue()))
            .toList();
    }

    public List<MyReservationResponse> findAllMine(Long memberId) {
        return reservationRepository.findAllByMemberId(memberId).stream()
            .map(MyReservationResponse::from)
            .toList();
    }
}
