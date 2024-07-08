package roomescape.reservation;

import org.springframework.stereotype.Service;
import auth.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.reservation.waiting.WaitingRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ReservationService {
    private final TimeRepository timeRepository;
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final ThemeRepository themeRepository;
    private final WaitingRepository waitingRepository;

    public ReservationService(TimeRepository timeRepository, ReservationRepository reservationRepository,
                              MemberRepository memberRepository, ThemeRepository themeRepository,
                              WaitingRepository waitingRepository) {
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.themeRepository = themeRepository;
        this.waitingRepository = waitingRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        if (reservationRequest.getName() == null) {
            reservationRequest = new ReservationRequest(loginMember.name(),
                    reservationRequest.getDate(), reservationRequest.getTheme(), reservationRequest.getTime());
        }

        Member memberById = memberRepository.findMemberById(loginMember.id());
        Time time = timeRepository.findById(reservationRequest.getTime())
                .orElseThrow(() -> new IllegalArgumentException("time not found"));
        Theme theme = themeRepository.findById(reservationRequest.getTheme())
                .orElseThrow(() -> new IllegalArgumentException("theme not found"));

        Reservation reservation = reservationRepository.save(new Reservation(reservationRequest.getName(), reservationRequest.getDate(), time, theme, memberById));
        return new ReservationResponse(reservation.getId(), reservationRequest.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(),
                        it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    public List<MyReservationsResponse> findMyReservations(LoginMember loginMember) {
        List<Reservation> reservations = reservationRepository.findByMemberId(loginMember.id());

        Stream<MyReservationsResponse> str1 = reservations.stream()
                .map(it -> new MyReservationsResponse(it.getId(), it.getTheme().getName(),
                        it.getDate(), it.getTime().getValue(), "예약"));

        Stream<MyReservationsResponse> str2 = waitingRepository.findWaitingsWithRankByMemberId(loginMember.id()).stream()
                .map(it -> new MyReservationsResponse(it.waiting().getId(), it.waiting().getTheme().getName(),
                        it.waiting().getDate(), it.waiting().getTime(), (it.rank() + 1) + "번째 예약대기"));

        return Stream.concat(str1, str2).collect(Collectors.toList());
    }
}
