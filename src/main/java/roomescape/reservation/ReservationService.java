package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.login.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.reservation.dto.MyReservationResponse;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.Waiting;
import roomescape.waiting.WaitingRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final MemberRepository memberRepository;
    private final WaitingRepository waitingRepository;

    public ReservationService(ReservationRepository reservationRepository, ThemeRepository themeRepository, TimeRepository timeRepository, MemberRepository memberRepository, WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.memberRepository = memberRepository;
        this.waitingRepository = waitingRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        String name = reservationRequest.name();
        if (name == null) {
            name = loginMember.getName();
        }
        Theme theme = themeRepository.findById(reservationRequest.theme())
                .orElseThrow();
        Time time = timeRepository.findById(reservationRequest.time())
                .orElseThrow();
        Member member = memberRepository.findByName(name)
                .orElseThrow();
        Reservation reservation = Reservation.builder().
                member(member)
                .date(reservationRequest.date())
                .time(time)
                .theme(theme)
                .build();
        reservationRepository.save(reservation);
        return new ReservationResponse(
                reservation.getMember().getName(),
                reservation.getId(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getTimeValue());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getMember().getName(), it.getId(), it.getTheme().getName(), it.getDate(), it.getTime().getTimeValue()))
                .toList();
    }

    public List<MyReservationResponse> findMyReservations(LoginMember member) {
        List<MyReservationResponse> result = new ArrayList<>();
        reservationRepository.findByMemberId(member.getId())
                .forEach(reservation ->
                    result.add(new MyReservationResponse(
                        reservation.getId(),
                        reservation.getTheme().getName(),
                        reservation.getDate(),
                        reservation.getTime().getTimeValue(),
                        "예약"
                ))
        );

        waitingRepository.findWaitingsWithRankByMemberId(member.getId())
                .forEach(waitingWithRank ->{
                    Waiting waiting = waitingWithRank.waiting();

                    result.add(new MyReservationResponse(
                            waiting.getId(),
                            waiting.getTheme().getName(),
                            waiting.getDate(),
                            waiting.getTime().getTimeValue(),
                            (waitingWithRank.rank()+1+"번째 예약대기")
                    ));
                });
        return result;
    }
}
