package roomescape.waiting;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.reservation.ReservationService;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;


    public WaitingService(WaitingRepository waitingRepository,
                          TimeRepository timeRepository,
                          ThemeRepository themeRepository,
                          MemberRepository memberRepository) {
        this.waitingRepository = waitingRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public WaitingResponse create(WaitingRequest request, Member member) {
        Time time = timeRepository.findById(request.getTime())
            .orElseThrow(() -> new IllegalArgumentException("시간 없음"));
        Theme theme = themeRepository.findById(request.getTheme())
            .orElseThrow(() -> new IllegalArgumentException("테마 없음"));

        checkAvailabilityOfCreate(member, request.getDate(), time, theme);

        Waiting waiting = new Waiting(member, request.getDate(), time, theme);
        waitingRepository.save(waiting);
        return WaitingResponse.from(waiting);
    }

    private void checkAvailabilityOfCreate(Member member, String date, Time time, Theme theme) {
        boolean alreadyExists = waitingRepository.existsByMemberAndDateAndTimeAndTheme(member, date, time, theme);
        if (alreadyExists) {
            throw new IllegalStateException("이미 동일한 대기 항목이 존재합니다.");
        }
    }

    public void delete(Long id) {
        waitingRepository.deleteById(id);
    }

    public List<WaitingResponse> findAllByMemberId(Long memberId) {
        return waitingRepository.findWaitingsWithRankByMemberId(memberId)
            .stream()
            .map(WaitingResponse::from)
            .toList();
    }
}
