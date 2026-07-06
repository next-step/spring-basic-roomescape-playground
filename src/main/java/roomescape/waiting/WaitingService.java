package roomescape.waiting;

import org.springframework.stereotype.Service;
import roomescape.auth.LoginMember;
import roomescape.auth.UnauthorizedException;
import roomescape.reservation.ReservationRepository;
import roomescape.reservation.ReservationRequest;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

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

    public WaitingResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        if (loginMember == null) {
            throw new UnauthorizedException();
        }

        validateWaitingRequest(reservationRequest, loginMember);

        Theme theme = themeRepository.findById(reservationRequest.getTheme()).orElseThrow();
        Time time = timeRepository.findById(reservationRequest.getTime()).orElseThrow();
        Waiting waiting = waitingRepository.save(
                new Waiting(loginMember.getId(), reservationRequest.getDate(), time, theme)
        );

        return new WaitingResponse(
                waiting.getId(),
                waiting.getTheme().getName(),
                waiting.getDate(),
                waiting.getTime().getValue()
        );
    }

    public void delete(Long id, LoginMember loginMember) {
        if (loginMember == null) {
            throw new UnauthorizedException();
        }

        Waiting waiting = waitingRepository.findById(id).orElseThrow();
        if (!waiting.getMemberId().equals(loginMember.getId())) {
            throw new UnauthorizedException();
        }

        waitingRepository.deleteById(id);
    }

    private void validateWaitingRequest(ReservationRequest reservationRequest, LoginMember loginMember) {
        boolean reserved = reservationRepository.existsByDateAndThemeIdAndTimeId(
                reservationRequest.getDate(),
                reservationRequest.getTheme(),
                reservationRequest.getTime()
        );
        if (!reserved) {
            throw new IllegalArgumentException();
        }

        boolean alreadyReserved = reservationRepository.existsByDateAndThemeIdAndTimeIdAndMemberId(
                reservationRequest.getDate(),
                reservationRequest.getTheme(),
                reservationRequest.getTime(),
                loginMember.getId()
        );
        if (alreadyReserved) {
            throw new IllegalArgumentException();
        }

        boolean alreadyWaiting = waitingRepository.existsByDateAndThemeIdAndTimeIdAndMemberId(
                reservationRequest.getDate(),
                reservationRequest.getTheme(),
                reservationRequest.getTime(),
                loginMember.getId()
        );
        if (alreadyWaiting) {
            throw new IllegalArgumentException();
        }
    }
}
