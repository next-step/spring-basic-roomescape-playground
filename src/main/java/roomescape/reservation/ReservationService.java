package roomescape.reservation;

import jakarta.transaction.Transactional;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

import java.util.List;
import roomescape.auth.LoginMember;
import roomescape.exception.MemberNotFoundException;
import roomescape.exception.ThemeNotFoundException;
import roomescape.exception.TimeNotFoundException;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.WaitingRepository;

@Service
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;
    private final WaitingRepository waitingRepository;

    public ReservationService(ReservationRepository reservationRepository, TimeRepository timeRepository,
                              ThemeRepository themeRepository, MemberRepository memberRepository,
                              MemberRepository memberRepository1, WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository1;
        this.waitingRepository = waitingRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Time time = timeRepository.findById(reservationRequest.time())
                .orElseThrow(TimeNotFoundException::new);
        Theme theme = themeRepository.findById(reservationRequest.theme())
                .orElseThrow(ThemeNotFoundException::new);
        Member member = memberRepository.findByName(reservationRequest.name())
                .orElseThrow(MemberNotFoundException::new);
        Reservation reservation = new Reservation(reservationRequest.name(), reservationRequest.date(), member, time, theme);

        Reservation savedReservation = reservationRepository.save(reservation);

        return new ReservationResponse(
                savedReservation.getId(),
                savedReservation.getName(),
                savedReservation.getTheme().getName(),
                savedReservation.getDate(),
                savedReservation.getTime().getValue()
        );
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    public List<MyReservationResponse> findByMember(LoginMember loginMember) {
        Member member = memberRepository.findById(loginMember.id()).orElseThrow(MemberNotFoundException::new);
        List<MyReservationResponse> reservationList = reservationRepository.findByMember(member).stream()
                .map(it -> new MyReservationResponse(
                        it.getId(),
                        it.getTheme().getName(),
                        it.getDate(),
                        it.getTime().getValue(),
                        "예약"))
                .toList();

        List<MyReservationResponse> watingList = waitingRepository
                .findWaitingsWithRankByMemberId(loginMember.id()).stream()
                .map(it -> new MyReservationResponse(
                        it.getWaiting().getId(),
                        it.getWaiting().getTheme().getName(),
                        it.getWaiting().getDate(),
                        it.getWaiting().getTime().getValue(),
                        it.getRank() + 1 + "번째 예약대기"))
                .toList();

        return Stream.concat(reservationList.stream(), watingList.stream()).toList();
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

}
