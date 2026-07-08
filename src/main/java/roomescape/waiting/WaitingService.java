package roomescape.waiting;

import org.springframework.stereotype.Service;
import roomescape.auth.LoginMember;
import roomescape.exception.DuplicateReservationException;
import roomescape.member.Role;
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

    public WaitingResponse save(WaitingRequest waitingRequest, LoginMember loginMember) {
        Time time = timeRepository.findById(waitingRequest.getTime())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));
        Theme theme = themeRepository.findById(waitingRequest.getTheme())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));

        validateNotDuplicated(waitingRequest, loginMember);

        Waiting waiting = waitingRepository.save(
                new Waiting(waitingRequest.getDate(), time, theme, loginMember.getId()));

        return new WaitingResponse(waiting.getId(), theme.getName(), waiting.getDate(), time.getTime());
    }

    private void validateNotDuplicated(WaitingRequest waitingRequest, LoginMember loginMember) {
        boolean alreadyReserved = reservationRepository.existsByMember_IdAndDateAndTime_IdAndTheme_Id(
                loginMember.getId(), waitingRequest.getDate(), waitingRequest.getTime(), waitingRequest.getTheme());
        if (alreadyReserved) {
            throw new DuplicateReservationException("이미 예약한 시간입니다.");
        }

        boolean alreadyWaiting = waitingRepository.existsByMemberIdAndDateAndTime_IdAndTheme_Id(
                loginMember.getId(), waitingRequest.getDate(), waitingRequest.getTime(), waitingRequest.getTheme());
        if (alreadyWaiting) {
            throw new DuplicateReservationException("이미 예약 대기 중입니다.");
        }
    }

    public void deleteById(Long id, LoginMember loginMember) {
        Waiting waiting = waitingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 대기입니다."));
        if (!waiting.getMemberId().equals(loginMember.getId()) && loginMember.getRole() != Role.ADMIN) {
            throw new IllegalStateException("본인의 예약 대기만 취소할 수 있습니다.");
        }
        waitingRepository.deleteById(id);
    }
}
