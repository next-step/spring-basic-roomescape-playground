package roomescape.reservation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.WaitingRepository;
import roomescape.waiting.WaitingWithRank;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;
    private final WaitingRepository waitingRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              TimeRepository timeRepository,
                              ThemeRepository themeRepository,
                              MemberRepository memberRepository,
                              WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
        this.waitingRepository = waitingRepository;
    }

    @Transactional
    public ReservationResponse save(ReservationRequest request, LoginMember loginMember) {
        Time time = timeRepository.findById(request.getTime()).orElseThrow();
        Theme theme = themeRepository.findById(request.getTheme());

        Reservation reservation;

        if (loginMember != null) {
            Member member = memberRepository.findById(loginMember.getId()).orElseThrow();
            reservation = new Reservation(
                    loginMember.getName(),
                    request.getDate(),
                    time,
                    theme,
                    member
            );
        } else {
            reservation = new Reservation(
                    request.getName(),
                    request.getDate(),
                    time,
                    theme
            );
        }

        Reservation savedReservation = reservationRepository.save(reservation);

        return new ReservationResponse(
                savedReservation.getId(),
                savedReservation.getName(),
                savedReservation.getTheme().getName(),
                savedReservation.getDate(),
                savedReservation.getTime().getTime()
        );
    }

    public List<MyReservationResponse> findMine(LoginMember loginMember) {
        List<Reservation> reservations = reservationRepository.findByMemberId(loginMember.getId());

        List<MyReservationResponse> reservationResponses = reservations.stream()
                .map(reservation -> new MyReservationResponse(
                        reservation.getId(),
                        reservation.getTheme().getName(),
                        reservation.getDate(),
                        reservation.getTime().getTime(),
                        "예약"
                ))
                .collect(Collectors.toList());

        List<WaitingWithRank> waitings = waitingRepository.findWaitingsWithRankByMemberId(loginMember.getId());
        List<MyReservationResponse> waitingResponses = waitings.stream()
                .map(reservation -> new MyReservationResponse(
                        reservation.getWaiting().getId(),
                        reservation.getWaiting().getTheme().getName(),
                        reservation.getWaiting().getDate(),
                        reservation.getWaiting().getTime().getTime(),
                        reservation.getRank() + "번째 예약대기"
                ))
                .toList();
        reservationResponses.addAll(waitingResponses);

        return reservationResponses;
    }

    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(
                        it.getId(),
                        it.getName(),
                        it.getTheme().getName(),
                        it.getDate(),
                        it.getTime().getTime()
                ))
                .toList();
    }
}
