package roomescape.reservation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeService;
import roomescape.waiting.WaitingRepository;
import roomescape.waiting.WaitingWithRank;
import roomescape.login.LoginMember;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final TimeService timeService;
    private final ThemeRepository themeRepository;
    private final WaitingRepository waitingRepository;

    public ReservationService(ReservationRepository reservationRepository, MemberRepository memberRepository,
                              TimeService timeService, ThemeRepository themeRepository,
                              WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.timeService = timeService;
        this.themeRepository = themeRepository;
        this.waitingRepository = waitingRepository;
    }

    @Transactional
    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        String finalName = reservationRequest.getName();
        Member member = null;

        if (loginMember != null) {
            member = memberRepository.findById(loginMember.getId()).orElse(null);
            if (finalName == null) {
                finalName = loginMember.getName();
            }
        }

        reservationRepository.findByDateAndThemeIdAndTimeId(reservationRequest.getDate(), reservationRequest.getTheme(), reservationRequest.getTime())
                .ifPresent(r -> { throw new IllegalArgumentException(); });

        Time time = timeService.findById(reservationRequest.getTime());
        Theme theme = themeRepository.findById(reservationRequest.getTheme()).orElseThrow(IllegalArgumentException::new);

        Reservation reservation = new Reservation(finalName, reservationRequest.getDate(), member, time, theme);
        Reservation saved = reservationRepository.save(reservation);

        return new ReservationResponse(saved.getId(), saved.getName(), saved.getTheme().getName(), saved.getDate(), saved.getTime().getValue());
    }

    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    public List<MyReservationResponse> findByMember(LoginMember loginMember) {
        if (loginMember == null) {
            throw new IllegalArgumentException();
        }

        List<MyReservationResponse> result = new ArrayList<>();

        List<Reservation> reservations = reservationRepository.findByMemberId(loginMember.getId());
        for (Reservation r : reservations) {
            result.add(new MyReservationResponse(r.getId(), r.getTheme().getName(), r.getDate(), r.getTime().getValue(), "예약"));
        }

        List<WaitingWithRank> waitings = waitingRepository.findWaitingsWithRankByMemberId(loginMember.getId());
        for (WaitingWithRank w : waitings) {
            result.add(new MyReservationResponse(
                    w.getWaiting().getId(),
                    w.getWaiting().getTheme().getName(),
                    w.getWaiting().getDate(),
                    w.getWaiting().getTime().getValue(),
                    w.getRank() + "번째 예약대기"
            ));
        }

        return result;
    }
}
