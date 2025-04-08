package roomescape.waiting;

import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final MemberRepository memberRepository;

    public WaitingService(
            WaitingRepository waitingRepository,
            ThemeRepository themeRepository,
            TimeRepository timeRepository,
            MemberRepository memberRepository) {
        this.waitingRepository = waitingRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.memberRepository = memberRepository;
    }

    public WaitingResponse createWaiting(WaitingRequest waitingRequest, Long memberId) {
        Long themeId = waitingRequest.theme();
        Long timeId = waitingRequest.time();
        String date = waitingRequest.date();

        Theme findTheme = themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 테마가 존재하지 않습니다."));
        Time findTime = timeRepository.findById(timeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 시간이 존재하지 않습니다."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        Waiting waiting = new Waiting(findTheme, date, findTime, member);
        Waiting savedWaiting = waitingRepository.save(waiting);
        Long id = waiting.getId();
        Long rank = waitingRepository.countByThemeAndDateAndTimeAndIdLessThanEqual(findTheme, date, findTime, id);
        return new WaitingResponse(id, rank);
    }

    public void deleteWaiting(Long waitingId, Long memberId) {
        Waiting waiting = waitingRepository.findById(waitingId)
                .orElseThrow(() -> new IllegalArgumentException("해당 대기열이 존재하지 않습니다."));
        if (waiting.isNotSameMember(memberId)) {
            throw new IllegalArgumentException("해당 대기열을 삭제할 권한이 없습니다.");
        }
        waitingRepository.delete(waiting);
    }
}
