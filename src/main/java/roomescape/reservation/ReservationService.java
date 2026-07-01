package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.auth.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.WaitingRepository;

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

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        Time time = timeRepository.findById(reservationRequest.getTime())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));
        Theme theme = themeRepository.findById(reservationRequest.getTheme())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));

        validateSlotNotReserved(reservationRequest);

        Reservation reservation = createReservation(reservationRequest, loginMember, time, theme);
        Reservation saved = reservationRepository.save(reservation);

        return new ReservationResponse(saved.getId(), saved.getName(), theme.getName(), saved.getDate(), time.getTime());
    }

    private void validateSlotNotReserved(ReservationRequest reservationRequest) {
        boolean alreadyReserved = reservationRepository.existsByDateAndTime_IdAndTheme_Id(
                reservationRequest.getDate(), reservationRequest.getTime(), reservationRequest.getTheme());
        if (alreadyReserved) {
            throw new IllegalStateException("이미 예약이 존재하는 시간입니다. 예약 대기를 이용해주세요.");
        }
    }

    private Reservation createReservation(ReservationRequest reservationRequest, LoginMember loginMember, Time time, Theme theme) {
        if (reservationRequest.getName() != null) {
            Member member = memberRepository.findByName(reservationRequest.getName())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
            return new Reservation(member.getName(), reservationRequest.getDate(), time, theme);
        }
        Member member = memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        return new Reservation(member, reservationRequest.getDate(), time, theme);
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getTime()))
                .toList();
    }

    public List<MyReservationResponse> findMyReservations(LoginMember loginMember) {
        List<MyReservationResponse> reservations = reservationRepository.findByMember_Id(loginMember.getId()).stream()
                .map(it -> new MyReservationResponse(
                        it.getId(),
                        it.getTheme().getName(),
                        it.getDate(),
                        it.getTime().getTime(),
                        "예약"))
                .toList();

        List<MyReservationResponse> waitings = waitingRepository.findWaitingsWithRankByMemberId(loginMember.getId()).stream()
                .map(it -> new MyReservationResponse(
                        it.getWaiting().getId(),
                        it.getWaiting().getTheme().getName(),
                        it.getWaiting().getDate(),
                        it.getWaiting().getTime().getTime(),
                        (it.getRank() + 1) + "번째 예약대기"))
                .toList();

        return Stream.concat(reservations.stream(), waitings.stream()).toList();
    }
}
