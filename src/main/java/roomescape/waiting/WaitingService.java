package roomescape.waiting;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.LoginMember;
import roomescape.reservation.ReservationRepository;
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

    public WaitingService(WaitingRepository waitingRepository,
                          ReservationRepository reservationRepository,
                          TimeRepository timeRepository,
                          ThemeRepository themeRepository) {
        this.waitingRepository = waitingRepository;
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional
    public WaitingResponse createWaiting(WaitingRequest request, Long memberId) {
        Time time = timeRepository.findById(request.getTime())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));

        Theme theme = themeRepository.findById(request.getTheme())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));

        if (reservationRepository.existsByMemberIdAndDateAndTimeAndTheme(memberId, request.getDate(), time, theme)) {
            throw new IllegalArgumentException("이미 예약한 테마입니다. 예약 대기를 신청할 수 없습니다.");
        }
        if (waitingRepository.existsByMemberIdAndDateAndTimeAndTheme(memberId, request.getDate(), time, theme)) {
            throw new IllegalArgumentException("이미 예약 대기를 신청했습니다.");
        }

        long waitingNumber = waitingRepository.countByDateAndTimeAndTheme(request.getDate(), time, theme) + 1;

        Waiting waiting = waitingRepository.save(new Waiting(
                memberId,
                request.getDate(),
                time,
                theme
        ));

        return new WaitingResponse(waiting.getId(), waitingNumber);
    }

    @Transactional
    public void deleteWaiting(Long id, LoginMember loginMember) {
        Waiting waiting = waitingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 대기입니다."));

        if (!"ADMIN".equals(loginMember.getRole()) && !waiting.getMemberId().equals(loginMember.getId())) {
            throw new IllegalArgumentException("본인의 예약 대기만 취소할 수 있습니다.");
        }

        waitingRepository.delete(waiting);
    }

    public List<WaitingWithRank> findWaitingsWithRankByMemberId(Long memberId) {
        return waitingRepository.findWaitingsWithRankByMemberId(memberId);
    }
}
