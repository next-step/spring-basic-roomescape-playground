package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.member.MemberRepository;
import roomescape.member.Member;
import roomescape.waiting.WaitingService;
import roomescape.waiting.WaitingWithRank;

import java.util.List;
import java.util.ArrayList;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;
    private final WaitingService waitingService;

    public ReservationService(ReservationRepository reservationRepository, TimeRepository timeRepository, ThemeRepository themeRepository, MemberRepository memberRepository, WaitingService waitingService) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
        this.waitingService = waitingService;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, Long loginMemberId) {
        Time time = timeRepository.findById(reservationRequest.getTime()).orElseThrow();
        Theme theme = themeRepository.findById(reservationRequest.getTheme()).orElseThrow();
        Reservation reservation = new Reservation(reservationRequest.getName(), reservationRequest.getDate(), time, theme);
        if (loginMemberId != null) {
            Member memberRef = memberRepository.getReferenceById(loginMemberId);
            // name은 컨트롤러에서 이미 로그인 사용자명으로 보정됨
            // 예약의 member 참조만 세팅
            try {
                var memberField = Reservation.class.getDeclaredField("member");
                memberField.setAccessible(true);
                memberField.set(reservation, memberRef);
            } catch (Exception ignore) {}
        }
        reservation = reservationRepository.save(reservation);

        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    public List<MyReservationResponse> findMine(Long memberId) {
        List<MyReservationResponse> result = new ArrayList<>();
        // 예약
        result.addAll(
                reservationRepository.findByMember_Id(memberId).stream()
                        .map(MyReservationResponse::from)
                        .toList()
        );
        // 대기 + 순번
        List<WaitingWithRank> waitings = waitingService.findMineWithRank(memberId);
        for (WaitingWithRank w : waitings) {
            long rankOneBased = (w.getRank() == null ? 0 : w.getRank()) + 1;
            result.add(new MyReservationResponse(
                    w.getWaiting().getId(),
                    w.getWaiting().getTheme().getName(),
                    w.getWaiting().getDate(),
                    w.getWaiting().getTime().getValue(),
                    rankOneBased + "번째 예약대기"
            ));
        }
        return result;
    }
}
