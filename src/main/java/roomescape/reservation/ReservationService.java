package roomescape.reservation;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Stream;
import roomescape.JwtTokenProvider;
import roomescape.member.Member;
import roomescape.member.MemberService;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.WaitingRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final WaitingRepository waitingRepository;

    public ReservationService(ReservationRepository reservationRepository, MemberService memberService, JwtTokenProvider jwtTokenProvider,
                              TimeRepository timeRepository, ThemeRepository themeRepository, WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.waitingRepository = waitingRepository;
    }

    @Transactional
    public ReservationResponse save(ReservationRequest reservationRequest, String token) {
        Long memberId = Long.parseLong(jwtTokenProvider.getMemberId(token));
        Member member = memberService.findById(memberId);

        Time time = timeRepository.findById(reservationRequest.time())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다"));
        Theme theme = themeRepository.findById(reservationRequest.theme())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다"));

        Reservation reservation = new Reservation(
                reservationRequest.name(), member, reservationRequest.date(), time, theme);
        Reservation saved = reservationRepository.save(reservation);

        return new ReservationResponse(saved.getId(), saved.getName(), saved.getTheme().getName(), saved.getDate(), saved.getTime().getTime());
    }

    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getTime()))
                .toList();
    }

    public List<MyReservationResponse> findMyReservations(String token) {
        Long memberId = Long.parseLong(jwtTokenProvider.getMemberId(token));

        Stream<MyReservationResponse> reservations = reservationRepository.findByMemberId(memberId).stream()
                .map(MyReservationResponse::from);

        Stream<MyReservationResponse> waitings = waitingRepository.findWaitingsWithRankByMemberId(memberId).stream()
                .map(MyReservationResponse::from);

        return Stream.concat(reservations, waitings).toList();
    }
}
