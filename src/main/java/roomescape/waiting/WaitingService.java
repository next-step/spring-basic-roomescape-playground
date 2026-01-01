package roomescape.waiting;

import org.springframework.stereotype.Service;
import roomescape.member.MemberRepository;
import roomescape.reservation.ReservationRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;

@Service
public class WaitingService {
    private final WaitingRepository waitingRepository;
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public WaitingService(WaitingRepository waitingRepository,
                          ReservationRepository reservationRepository,
                          MemberRepository memberRepository,
                          TimeRepository timeRepository,
                          ThemeRepository themeRepository) {
        this.waitingRepository = waitingRepository;
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public WaitingResponse create(Long memberId, String date, Long timeId, Long themeId) {
        if (reservationRepository.existsByMember_IdAndDateAndTime_IdAndTheme_Id(memberId, date, timeId, themeId)) {
            throw new IllegalStateException();
        }
        if (waitingRepository.existsByMember_IdAndDateAndTime_IdAndTheme_Id(memberId, date, timeId, themeId)) {
            throw new IllegalStateException();
        }

        var memberRef = memberRepository.getReferenceById(memberId);
        Time time = timeRepository.findById(timeId).orElseThrow();
        Theme theme = themeRepository.findById(themeId).orElseThrow();
        Waiting waiting = new Waiting(memberRef, date, time, theme);
        waiting = waitingRepository.save(waiting);
        return new WaitingResponse(waiting.getId());
    }

    public void cancel(Long waitingId, Long memberId) {
        Waiting waiting = waitingRepository.findById(waitingId).orElseThrow();
        if (!waiting.getMember().getId().equals(memberId)) {
            throw new IllegalStateException();
        }
        waitingRepository.deleteById(waitingId);
    }

    public List<WaitingWithRank> findMineWithRank(Long memberId) {
        return waitingRepository.findWaitingsWithRankByMemberId(memberId);
    }
}


