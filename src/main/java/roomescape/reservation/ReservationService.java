package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.auth.LoginMember;
import roomescape.auth.UnauthorizedException;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.WaitingWithRank;
import roomescape.waiting.WaitingRepository;

import java.util.List;
import java.util.stream.Stream;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final WaitingRepository waitingRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            MemberRepository memberRepository,
            ThemeRepository themeRepository,
            TimeRepository timeRepository,
            WaitingRepository waitingRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.waitingRepository = waitingRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        validateDuplicateReservation(reservationRequest);

        Theme theme = themeRepository.findById(reservationRequest.getTheme()).orElseThrow();
        Time time = timeRepository.findById(reservationRequest.getTime()).orElseThrow();
        Reservation reservation = reservationRepository.save(createReservation(reservationRequest, loginMember, theme, time));

        return new ReservationResponse(
                reservation.getId(),
                getReservationName(reservation),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue()
        );
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(
                        it.getId(),
                        getReservationName(it),
                        it.getTheme().getName(),
                        it.getDate(),
                        it.getTime().getValue()
                ))
                .toList();
    }

    public List<MyReservationResponse> findMine(LoginMember loginMember) {
        if (loginMember == null) {
            throw new UnauthorizedException();
        }

        Stream<MyReservationResponse> reservations = reservationRepository.findByMemberId(loginMember.getId()).stream()
                .map(this::toMyReservationResponse);
        Stream<MyReservationResponse> waitings = waitingRepository.findWaitingsWithRankByMemberId(loginMember.getId()).stream()
                .map(this::toMyWaitingResponse);

        return Stream.concat(reservations, waitings)
                .toList();
    }

    private Reservation createReservation(
            ReservationRequest reservationRequest,
            LoginMember loginMember,
            Theme theme,
            Time time
    ) {
        if (hasName(reservationRequest)) {
            return new Reservation(reservationRequest.getName(), reservationRequest.getDate(), time, theme);
        }

        if (loginMember == null) {
            throw new UnauthorizedException();
        }

        Member member = memberRepository.findById(loginMember.getId()).orElseThrow();
        return new Reservation("", reservationRequest.getDate(), member, time, theme);
    }

    private boolean hasName(ReservationRequest reservationRequest) {
        return reservationRequest.getName() != null
                && !reservationRequest.getName().isBlank();
    }

    private void validateDuplicateReservation(ReservationRequest reservationRequest) {
        boolean duplicated = reservationRepository.existsByDateAndThemeIdAndTimeId(
                reservationRequest.getDate(),
                reservationRequest.getTheme(),
                reservationRequest.getTime()
        );
        if (duplicated) {
            throw new IllegalArgumentException();
        }
    }

    private String getReservationName(Reservation reservation) {
        if (reservation.getName() != null && !reservation.getName().isBlank()) {
            return reservation.getName();
        }

        return reservation.getMember().getName();
    }

    private MyReservationResponse toMyReservationResponse(Reservation reservation) {
        return new MyReservationResponse(
                reservation.getId(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue(),
                "예약"
        );
    }

    private MyReservationResponse toMyWaitingResponse(WaitingWithRank waitingWithRank) {
        return new MyReservationResponse(
                waitingWithRank.getWaiting().getId(),
                waitingWithRank.getWaiting().getTheme().getName(),
                waitingWithRank.getWaiting().getDate(),
                waitingWithRank.getWaiting().getTime().getValue(),
                waitingWithRank.getRank() + 1 + "번째 예약대기"
        );
    }
}
