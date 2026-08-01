package roomescape.waiting;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.Member;
import roomescape.reservation.ReservationRepository;
import roomescape.reservation.ReservationRequest;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public WaitingService(WaitingRepository waitingRepository, ReservationRepository reservationRepository, TimeRepository timeRepository, ThemeRepository themeRepository) {
        this.waitingRepository = waitingRepository;
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional
    public WaitingResponse createWaiting(ReservationRequest request, Member loginMember) {

        boolean hasReservation = reservationRepository.existsByDateAndTimeAndTheme(
                request.getDate(), request.getTime(), request.getTheme());
        if (!hasReservation) {
            throw new IllegalArgumentException("예약이 존재하지 않는 타임에는 대기를 신청할 수 없습니다.");
        }

        boolean isAlreadyWaiting = waitingRepository.existsByDateAndTimeAndThemeAndMember(
                request.getDate(), request.getTime(), request.getTheme(), loginMember.getId());
        if (isAlreadyWaiting) {
            throw new IllegalArgumentException("이미 대기를 신청한 타임입니다.");
        }

        Time time = timeRepository.findById(request.getTime())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));
        Theme theme = themeRepository.findById(request.getTheme())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));

        Waiting waiting = new Waiting(request.getDate(), time, theme, loginMember);
        Waiting saved = waitingRepository.save(waiting);

        return new WaitingResponse(saved.getId(), saved.getDate(), theme.getName(), time.getValue());
    }

    @Transactional
    public void deleteWaiting(Long id) {
        waitingRepository.deleteById(id);
    }

    public List<WaitingWithRank> getWaitingsWithRank(Long memberId) {
        return waitingRepository.findWaitingsWithRankByMemberId(memberId);
    }


}
