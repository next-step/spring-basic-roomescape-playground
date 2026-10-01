package roomescape.waiting;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.login.LoginMember;
import roomescape.reservation.ReservationRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;

    public WaitingService(
            WaitingRepository waitingRepository,
            ReservationRepository reservationRepository,
            ThemeRepository themeRepository,
            TimeRepository timeRepository
    ) {
        this.waitingRepository = waitingRepository;
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
    }

    @Transactional
    public WaitingResponse save(WaitingRequest request, LoginMember loginMember) {
        reservationRepository.findBySlotForUpdate(
                request.getDate(),
                request.getTime(),
                request.getTheme()
        ).orElseThrow(() ->
                new IllegalArgumentException("예약이 존재하지 않아 대기할 수 없습니다.")
        );

        boolean waitingExists =
                waitingRepository
                        .existsByMemberIdAndDateAndTimeIdAndThemeId(
                                loginMember.id(),
                                request.getDate(),
                                request.getTime(),
                                request.getTheme()
                        );

        if (waitingExists) {
            throw new IllegalArgumentException("이미 예약 대기 중입니다.");
        }

        Theme theme = themeRepository
                .findById(request.getTheme())
                .orElseThrow(NoSuchElementException::new);

        Time time = timeRepository
                .findById(request.getTime())
                .orElseThrow(NoSuchElementException::new);

        Long maxWaitingOrder =
                waitingRepository.findMaxWaitingOrder(
                        request.getTheme(),
                        request.getDate(),
                        request.getTime()
                );

        Long nextWaitingOrder = maxWaitingOrder + 1;

        Waiting waiting = new Waiting(
                loginMember.id(),
                request.getDate(),
                time,
                theme,
                nextWaitingOrder
        );

        Waiting savedWaiting = waitingRepository.save(waiting);

        return new WaitingResponse(
                savedWaiting.getId(),
                savedWaiting.getTheme().getName(),
                savedWaiting.getDate(),
                savedWaiting.getTime().getValue()
        );
    }

    public void delete(Long id, LoginMember loginMember) {
        Waiting waiting = waitingRepository.findByIdAndMemberId(id, loginMember.id()).orElseThrow(NoSuchElementException::new);
        waitingRepository.delete(waiting);
    }

    public List<WaitingWithRank> findByMemberId(Long memberId) {
        return waitingRepository.findWaitingsWithRankByMemberId(memberId);
    }
}
