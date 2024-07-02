package roomescape.waiting;

import org.springframework.stereotype.Service;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.reservation.MyReservationResponse;
import roomescape.reservation.ReservationRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WaitingService {
    private final WaitingRepository waitingRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;

    public WaitingService(WaitingRepository waitingRepository, ThemeRepository themeRepository,
                          TimeRepository timeRepository, MemberRepository memberRepository,
                          ReservationRepository reservationRepository) {
        this.waitingRepository = waitingRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.memberRepository = memberRepository;
        this.reservationRepository = reservationRepository;
    }

    public WaitingResponse createWaitingReservation(WaitingRequest waitingRequest, LoginMember loginMember) {
        Member member = memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        Time time = timeRepository.findById(waitingRequest.getTime())
                .orElseThrow(() -> new IllegalArgumentException("Time not found"));

        Theme theme = themeRepository.findById(waitingRequest.getTheme())
                .orElseThrow(() -> new IllegalArgumentException("Theme not found"));

        // 예약 대기 목록
        List<Waiting> existingWaitings = waitingRepository.findByThemeAndDateAndTime(theme, waitingRequest.getDate(), time);

        Long rank = (long) existingWaitings.size() + 1;

        Waiting waiting = new Waiting();
        waiting.setDate(waitingRequest.getDate());
        waiting.setTheme(theme);
        waiting.setTime(time);
        waiting.setMember(member);
        waiting.setRank(rank);
        waiting.setStatus(rank + "번째 예약대기");


        waiting = waitingRepository.save(waiting);

        return new WaitingResponse(waiting.getId(), waiting.getDate(), theme.getName(), time.getValue(), waiting.getStatus(), waiting.getRank());
    }

    public List<MyReservationResponse> findWaitingsByMember(LoginMember loginMember) {
        List<WaitingWithRank> waitingsWithRank = waitingRepository.findWaitingsWithRankByMemberId(loginMember.getId());

        return waitingsWithRank.stream()
                .map(waitingWithRank -> {
                    Waiting waiting = waitingWithRank.getWaiting();
                    return new MyReservationResponse(
                            waiting.getId(),
                            waiting.getTheme().getName(),
                            waiting.getDate(),
                            waiting.getTime().getValue(),
                            waiting.getStatus(),
                            waiting.getRank()
                    );
                })
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        waitingRepository.deleteById(id);
    }
}