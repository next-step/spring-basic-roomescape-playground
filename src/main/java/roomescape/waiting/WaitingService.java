package roomescape.waiting;

import roomescape.annotation.MemberSession;
import roomescape.member.model.Member;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;

public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public WaitingService(WaitingRepository waitingRepository, TimeRepository timeRepository, ThemeRepository themeRepository) {
        this.waitingRepository = waitingRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public WaitingResponse addWaiting(WaitingRequest request, @MemberSession Member member) {
        Time time = timeRepository.findById(request.getTime()).orElseThrow();
        Theme theme = themeRepository.findById(request.getThemeId()).orElseThrow();

        List<Waiting> waitingList = waitingRepository.findByDateAndTimeIdAndThemeId(request.getDate(), time.getValue(), request.getThemeId());

        waitingList.stream()
                .filter(waiting -> waiting.getId().equals(member.getId()))
                .findFirst()
                .ifPresent(waiting -> {
                    throw new IllegalArgumentException("이미 대기중인 예약이 있습니다.");
                });
        Waiting waiting = waitingRepository.save(new Waiting(member.getId(), theme, request.getDate(), time.getValue()));
        return new WaitingResponse(waiting.getId(), theme.getId(), waiting.getDate(), waiting.getTime(), waitingList.size());
    }

    public void deleteWaiting(Long waitingId, @MemberSession Member member) {
        Waiting waiting = waitingRepository.findById(waitingId).orElseThrow();
        if (!waiting.getId().equals(member.getId())) {
            throw new IllegalArgumentException("예약을 취소할 수 없습니다.");
        }
        waitingRepository.delete(waiting);
    }
}