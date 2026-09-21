package roomescape.waiting;

import org.springframework.stereotype.Service;
import roomescape.exception.DuplicateException;
import roomescape.exception.NotFoundException;
import roomescape.exception.UnauthorizedException;
import roomescape.member.LoginMember;
import roomescape.reservation.ReservationRepository;
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

    public WaitingService(WaitingRepository waitingRepository,
                          ReservationRepository reservationRepository,
                          ThemeRepository themeRepository,
                          TimeRepository timeRepository) {
        this.waitingRepository = waitingRepository;
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
    }

    public WaitingResponse save(WaitingRequest request, LoginMember loginMember) {
        Theme theme = themeRepository.findById(request.getTheme())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 테마입니다."));
        Time time = timeRepository.findById(request.getTime())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 시간입니다."));

        // 예약이 비어있을 때 예약 대기를 누른 경우
        boolean reservationExists = reservationRepository.existsByDateAndTheme_IdAndTime_Id(
                request.getDate(), request.getTheme(), request.getTime());
        if (!reservationExists) {
            throw new NotFoundException("해당 시간에 예약이 존재하지 않아 대기를 등록할 수 없습니다.");
        }

        boolean alreadyReserved = reservationRepository.existsByDateAndTheme_IdAndTime_IdAndMember_Id(
                request.getDate(), request.getTheme(), request.getTime(), loginMember.getId());
        if (alreadyReserved) {
            throw new DuplicateException("이미 예약한 테마입니다.");
        }

        boolean alreadyWaiting = waitingRepository.existsByDateAndTheme_IdAndTime_IdAndMemberId(
                request.getDate(), request.getTheme(), request.getTime(), loginMember.getId());
        if (alreadyWaiting) {
            throw new DuplicateException("이미 예약 대기 중입니다.");
        }

        Waiting waiting = new Waiting(loginMember.getName(), request.getDate(), time, theme, loginMember.getId());
        Waiting saved = waitingRepository.save(waiting);

        // 방금 저장된 대기가 마지막 대기라서 저장 후 갯수가 곧 순번
        long waitingNumber = waitingRepository.countByDateAndTheme_IdAndTime_Id(
                request.getDate(), request.getTheme(), request.getTime());

        return new WaitingResponse(saved.getId(), saved.getTheme().getName(), saved.getDate(),
                saved.getTime().getTime(), waitingNumber);
    }

    public void deleteById(Long id, LoginMember loginMember) {
        Waiting waiting = waitingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 예약 대기입니다."));

        if (!waiting.getMemberId().equals(loginMember.getId())) {
            throw new UnauthorizedException("본인의 예약 대기만 취소할 수 있습니다.");
        }

        waitingRepository.deleteById(id);
    }
}
