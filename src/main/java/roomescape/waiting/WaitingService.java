package roomescape.waiting;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.LoginMemberInfo;
import roomescape.exception.ErrorCode;
import roomescape.exception.NotFoundException;
import roomescape.reservation.ReservationMineResponse;
import roomescape.theme.Theme;
import roomescape.theme.ThemeDao;
import roomescape.time.Time;
import roomescape.time.TimeDao;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class WaitingService {
    private final WaitingDao waitingDao;
    private final TimeDao timeDao;
    private final ThemeDao themeDao;

    public WaitingService(WaitingDao waitingDao, TimeDao timeDao, ThemeDao themeDao) {
        this.waitingDao = waitingDao;
        this.timeDao = timeDao;
        this.themeDao = themeDao;
    }

    @Transactional
    public WaitingResponse save(WaitingRequest request, LoginMemberInfo loginMember) {
        Theme theme = themeDao.findByIdAndDeletedFalse(request.getTheme())
                .orElseThrow(() -> new NotFoundException(ErrorCode.THEME_NOT_FOUND));
        Time time = timeDao.findByIdAndDeletedFalse(request.getTime())
                .orElseThrow(() -> new NotFoundException(ErrorCode.TIME_NOT_FOUND));
        Waiting waiting = waitingDao.save(new Waiting(loginMember.id(), request.getDate(), time, theme));
        return new WaitingResponse(waiting.id(), theme.name(), waiting.date(), time.value());
    }

    public List<ReservationMineResponse> findMine(Long memberId) {
        return waitingDao.findWaitingsWithRankByMemberId(memberId).stream()
                .map(waitingWithRank -> toMineResponse(waitingWithRank, waitingWithRank.getRank() + 1))
                .toList();
    }

    private ReservationMineResponse toMineResponse(WaitingWithRank waitingWithRank, long rank) {
        Waiting waiting = waitingWithRank.getWaiting();
        return new ReservationMineResponse(
                waiting.id(), waiting.theme().name(), waiting.date(), waiting.time().value(),
                rank + "번째 예약대기"
        );
    }
}
