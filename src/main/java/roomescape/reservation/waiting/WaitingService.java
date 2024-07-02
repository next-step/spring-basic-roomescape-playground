package roomescape.reservation.waiting;

import org.springframework.stereotype.Service;
import roomescape.auth.LoginMember;
import roomescape.theme.Theme;
import roomescape.theme.ThemeDao;
import roomescape.time.Time;
import roomescape.time.TimeDao;

import java.util.List;

@Service
public class WaitingService {
    private final WaitingDao waitingDao;
    private final TimeDao timeDao;
    private final ThemeDao themeDao;

    public WaitingService(WaitingDao waitingDao, TimeDao timeDao, ThemeDao themeDao) {
        this.waitingDao = waitingDao;
        this.timeDao = timeDao;
        this.themeDao = themeDao;
    }

    public WaitingResponse createWaiting(WaitingRequest waitingRequest, LoginMember loginMember) {
        Time time = timeDao.findById(waitingRequest.getTime());
        Theme theme = themeDao.findById(waitingRequest.getTheme());
        List<Waiting> waitings = waitingDao.findByThemeIdAndDateAndTime(theme.getId(), waitingRequest.getDate(), time.getValue());
        int cnt = waitings.size();

        waitings.stream()
                .filter(it -> it.getTheme().getId() == waitingRequest.getTheme())
                .filter(it -> it.getMemberId().equals(loginMember.id()))
                .filter(it -> it.getDate().equals(waitingRequest.getDate()))
                .filter(it -> it.getTime().equals(waitingRequest.getTime()))
                .findAny()
                .ifPresent(it -> {
                    throw new IllegalArgumentException();
                });

        Waiting waiting = waitingDao.save(new Waiting(theme, loginMember.id(), waitingRequest.getDate(), time.getValue()));
        return new WaitingResponse(waiting.getId(), waiting.getDate(),
                waiting.getTime(), waiting.getTheme().getId(), cnt);
    }

    public void cancelWaiting(Long id) {
        waitingDao.deleteById(id);
    }
}
