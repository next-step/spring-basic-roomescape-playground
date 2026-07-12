package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberNotFoundException;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.WaitingRepository;
import roomescape.waiting.WaitingWithRank;

import java.util.List;
import java.util.stream.Stream;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final WaitingRepository waitingRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              MemberRepository memberRepository,
                              TimeRepository timeRepository,
                              ThemeRepository themeRepository,
                              WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.waitingRepository = waitingRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, Member loginMember) {
        Time time = timeRepository.findById(reservationRequest.getTime())
                .orElseThrow(() -> new IllegalArgumentException("예약 시간을 찾을 수 없습니다."));
        Theme theme = themeRepository.findById(reservationRequest.getTheme())
                .orElseThrow(() -> new IllegalArgumentException("테마를 찾을 수 없습니다."));

        Reservation reservation;
        if (reservationRequest.getName() != null && !reservationRequest.getName().isBlank()) {
            reservation = new Reservation(reservationRequest.getName(), reservationRequest.getDate(), time, theme);
        } else {
            validateNotAlreadyWaiting(loginMember.getId(), reservationRequest.getDate(), reservationRequest.getTime(), reservationRequest.getTheme());
            reservation = new Reservation(loginMember, reservationRequest.getDate(), time, theme);
        }

        Reservation saved = reservationRepository.save(reservation);

        return new ReservationResponse(
                saved.getId(),
                resolveName(saved),
                saved.getTheme().getName(),
                saved.getDate(),
                saved.getTime().getValue()
        );
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    public List<MyReservationResponse> findMyReservations(Member loginMember) {
        List<MyReservationResponse> reservations = reservationRepository.findByMemberId(loginMember.getId()).stream()
                .map(it -> new MyReservationResponse(
                        it.getId(),
                        it.getTheme().getName(),
                        it.getDate(),
                        it.getTime().getValue(),
                        "예약"))
                .toList();

        List<MyReservationResponse> waitings = waitingRepository.findWaitingsWithRankByMemberId(loginMember.getId()).stream()
                .map(this::toMyReservationResponse)
                .toList();

        return Stream.concat(reservations.stream(), waitings.stream()).toList();
    }

    private MyReservationResponse toMyReservationResponse(WaitingWithRank waitingWithRank) {
        return new MyReservationResponse(
                waitingWithRank.getWaiting().getId(),
                waitingWithRank.getWaiting().getTheme().getName(),
                waitingWithRank.getWaiting().getDate(),
                waitingWithRank.getWaiting().getTime().getValue(),
                (waitingWithRank.getRank() + 1) + "번째 예약대기");
    }

    private void validateNotAlreadyWaiting(Long memberId, String date, Long timeId, Long themeId) {
        boolean alreadyWaiting = waitingRepository.existsByMemberIdAndDateAndTimeIdAndThemeId(memberId, date, timeId, themeId);
        if (alreadyWaiting) {
            throw new IllegalArgumentException("이미 예약 대기 중인 테마입니다. 예약 대기를 취소한 후 예약해주세요.");
        }
    }

    private String resolveName(Reservation reservation) {
        if (reservation.getMember() != null) {
            return reservation.getMember().getName();
        }
        return reservation.getName();
    }
}
