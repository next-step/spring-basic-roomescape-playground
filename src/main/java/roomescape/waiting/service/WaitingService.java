package roomescape.waiting.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.dto.LoginMember;
import roomescape.exception.BadRequestException;
import roomescape.exception.ExceptionMessage;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.Time;
import roomescape.time.repository.TimeRepository;
import roomescape.waiting.domain.Waiting;
import roomescape.waiting.dto.request.WaitingRequest;
import roomescape.waiting.dto.response.WaitingResponse;
import roomescape.waiting.repository.WaitingRepository;

@Service
@Transactional(readOnly = true)
public class WaitingService {

    private final ReservationRepository reservationRepository;
    private final WaitingRepository waitingRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public WaitingService(ReservationRepository reservationRepository, WaitingRepository waitingRepository, TimeRepository timeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.waitingRepository = waitingRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional
    public WaitingResponse createWaiting(WaitingRequest waitingRequest, LoginMember loginMember) {
        Time time = findTime(waitingRequest.time());
        Theme theme = findTheme(waitingRequest.theme());
        Waiting waiting = waitingRequest.toWaiting(loginMember.id(), loginMember.name(), time, theme);

        validateIsAlreadyReserved(waiting);
        validateUniqueWaiting(waiting);
        Waiting savedWaiting = waitingRepository.save(waiting);

        Long waitingNumber = waitingRepository.findWaitingNumberByMemberId(loginMember.id());
        return new WaitingResponse(savedWaiting.getId(), waitingNumber);
    }

    private Time findTime(long timeId) {
        return timeRepository.findById(timeId)
                .orElseThrow(() -> new BadRequestException(ExceptionMessage.INVALID_TIME.getMessage()));
    }

    private Theme findTheme(long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new BadRequestException(ExceptionMessage.INVALID_THEME.getMessage()));
    }

    private void validateIsAlreadyReserved(Waiting waiting) {
        if (!reservationRepository.existsByDateAndTimeAndTheme(waiting.getDate(), waiting.getTime(), waiting.getTheme())) {
            throw new BadRequestException(ExceptionMessage.RESERVATION_NOT_FOUND.getMessage());
        }
    }

    private void validateUniqueWaiting(Waiting waiting) {
        if (reservationRepository.existsByMemberIdAndDateAndTimeAndTheme(waiting.getMemberId(), waiting.getDate(), waiting.getTime(), waiting.getTheme())) {
            throw new BadRequestException(ExceptionMessage.RESERVATION_ALREADY_EXISTS.getMessage());
        }
        if (waitingRepository.existsByMemberIdAndDateAndTimeAndTheme(waiting.getMemberId(), waiting.getDate(), waiting.getTime(), waiting.getTheme())) {
            throw new BadRequestException(ExceptionMessage.WAITING_ALREADY_EXISTS.getMessage());
        }
    }

    @Transactional
    public void delete(Long id) {
        waitingRepository.deleteById(id);
    }
}
