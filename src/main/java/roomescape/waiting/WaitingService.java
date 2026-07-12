package roomescape.waiting;

import org.springframework.stereotype.Service;
import roomescape.login.UnauthorizedException;
import roomescape.member.Member;
import roomescape.reservation.ReservationRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
public class WaitingService {
    private final WaitingRepository waitingRepository;
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public WaitingService(WaitingRepository waitingRepository,
                           ReservationRepository reservationRepository,
                           TimeRepository timeRepository,
                           ThemeRepository themeRepository) {
        this.waitingRepository = waitingRepository;
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public WaitingResponse save(WaitingRequest waitingRequest, Member loginMember) {
        Time time = timeRepository.findById(waitingRequest.getTime())
                .orElseThrow(() -> new IllegalArgumentException("예약 시간을 찾을 수 없습니다."));
        Theme theme = themeRepository.findById(waitingRequest.getTheme())
                .orElseThrow(() -> new IllegalArgumentException("테마를 찾을 수 없습니다."));

        validateNotDuplicated(loginMember.getId(), waitingRequest.getDate(), waitingRequest.getTime(), waitingRequest.getTheme());

        long rank = waitingRepository.countByThemeIdAndDateAndTimeId(waitingRequest.getTheme(), waitingRequest.getDate(), waitingRequest.getTime());
        Waiting saved = waitingRepository.save(new Waiting(loginMember, waitingRequest.getDate(), time, theme));

        return new WaitingResponse(saved.getId(), theme.getName(), saved.getDate(), time.getValue(), rank + 1);
    }

    public void deleteById(Long id, Member loginMember) {
        Waiting waiting = waitingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("예약 대기를 찾을 수 없습니다."));
        if (!waiting.getMember().getId().equals(loginMember.getId())) {
            throw new UnauthorizedException("본인의 예약 대기만 취소할 수 있습니다.");
        }
        waitingRepository.deleteById(id);
    }

    private void validateNotDuplicated(Long memberId, String date, Long timeId, Long themeId) {
        boolean alreadyReserved = reservationRepository.existsByMemberIdAndDateAndTimeIdAndThemeId(memberId, date, timeId, themeId);
        boolean alreadyWaiting = waitingRepository.existsByMemberIdAndDateAndTimeIdAndThemeId(memberId, date, timeId, themeId);
        if (alreadyReserved || alreadyWaiting) {
            throw new IllegalArgumentException("이미 예약 또는 예약 대기 중인 테마입니다.");
        }
    }
}
