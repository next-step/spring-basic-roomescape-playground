package roomescape.reservation.waiting;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import auth.LoginMember;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WaitingService {
    private final WaitingRepository waitingRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public WaitingResponse createWaiting(WaitingRequest waitingRequest, LoginMember loginMember) {
        Time time = timeRepository.findById(waitingRequest.getTime())
                .orElseThrow(() -> new IllegalArgumentException("Time not found"));
        Theme theme = themeRepository.findById(waitingRequest.getTheme())
                .orElseThrow(() -> new IllegalArgumentException("Theme not found"));
        List<Waiting> waitings = waitingRepository.findByThemeIdAndDateAndTime(theme.getId(), waitingRequest.getDate(), time.getValue());
        int cnt = waitings.size();

        waitings.stream()
                .filter(it -> it.getTheme().getId() == waitingRequest.getTheme())
                .filter(it -> it.getMemberId().equals(loginMember.id()))
                .filter(it -> it.getDate().equals(waitingRequest.getDate()))
                .filter(it -> it.getTime().equals(waitingRequest.getTime()))
                .findAny()
                .ifPresent(it -> {
                    throw new IllegalArgumentException("대기 없음");
                });

        Waiting waiting = waitingRepository.save(new Waiting(theme, loginMember.id(), waitingRequest.getDate(), time.getValue()));
        return new WaitingResponse(waiting.getId(), waiting.getDate(),
                waiting.getTime(), waiting.getTheme().getId(), cnt);
    }

    public void cancelWaiting(Long id) {
        waitingRepository.deleteById(id);
    }
}
