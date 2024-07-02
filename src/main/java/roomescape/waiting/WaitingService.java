package roomescape.waiting;

import org.springframework.stereotype.Service;
import roomescape.member.LoginMember;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;

@Service
public class WaitingService {
    private final WaitingRepository waitingRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public WaitingService(WaitingRepository waitingRepository, TimeRepository timeRepository, ThemeRepository themeRepository) {
        this.waitingRepository = waitingRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public WaitingResponse addWaiting(WaitingRequest request, LoginMember loginMember) {
        Time time = timeRepository.findById(request.getTime()).orElseThrow();
        Theme theme = themeRepository.findById(request.getTheme()).orElseThrow();

        List<Waiting> waitingList = waitingRepository.findByDateAndTimeAndThemeId(request.getDate(), time.getValue(), request.getTheme());

        waitingList.stream()
                .filter(waiting -> waiting.isMyReservation(loginMember.getId()))
                .filter(waiting -> waiting.getTheme().getId().equals(request.getTheme()))
                .filter(waiting -> waiting.getDate().equals(request.getDate()))
                .filter(waiting -> waiting.getTime().equals(time.getValue()))
                .findFirst()
                .ifPresent(waiting -> {
                    throw new IllegalArgumentException("이미 대기중인 예약이 있습니다.");
                });

        Waiting waiting = waitingRepository.save(new Waiting(loginMember.getId(), theme, request.getDate(), time.getValue()));
        return new WaitingResponse(waiting.getId(), theme.getId(), waiting.getDate(), waiting.getTime(), waitingList.size());
    }

    public void deleteWaiting(Long waitingId, LoginMember loginMember) {
        Waiting waiting = waitingRepository.findById(waitingId).orElseThrow();
        if (!waiting.isMyReservation(loginMember.getId())) {
            throw new IllegalArgumentException("예약을 취소할 수 없습니다.");
        }
        waitingRepository.delete(waiting);
    }
}
