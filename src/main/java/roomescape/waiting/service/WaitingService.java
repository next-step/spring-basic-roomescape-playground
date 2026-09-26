package roomescape.waiting.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.auth.AuthorizationException;
import roomescape.member.domain.LoginMember;
import roomescape.member.domain.Member;
import roomescape.member.service.MemberService;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.Time;
import roomescape.time.repository.TimeRepository;
import roomescape.waiting.domain.Waiting;
import roomescape.waiting.repository.WaitingRepository;

@Service
@Transactional(readOnly = true)
public class WaitingService {
    private static final String DUPLICATE_WAITING_MESSAGE = "이미 예약 대기한 날짜, 테마, 시간입니다.";

    private final WaitingRepository waitingRepository;
    private final ReservationRepository reservationRepository;
    private final MemberService memberService;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;

    public WaitingService(WaitingRepository waitingRepository,
                          ReservationRepository reservationRepository,
                          MemberService memberService,
                          ThemeRepository themeRepository,
                          TimeRepository timeRepository) {
        this.waitingRepository = waitingRepository;
        this.reservationRepository = reservationRepository;
        this.memberService = memberService;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
    }

    @Transactional
    public WaitingResult save(WaitingCommand command, LoginMember loginMember) {
        validateRequest(command, loginMember.id());

        Member member = memberService.findById(loginMember.id());
        Theme theme = themeRepository.findById(command.themeId())
                .orElseThrow(() -> new IllegalArgumentException("예약 테마를 찾을 수 없습니다."));
        Time time = timeRepository.findById(command.timeId())
                .orElseThrow(() -> new IllegalArgumentException("예약 시간을 찾을 수 없습니다."));

        try {
            Waiting waiting = waitingRepository.save(new Waiting(member, command.date(), time, theme));
            long waitingNumber = waitingRepository.countByDateAndTheme_IdAndTime_Id(
                    command.date(), command.themeId(), command.timeId());
            return toResult(waiting, waitingNumber);
        } catch (DataIntegrityViolationException exception) {
            throw new IllegalArgumentException(DUPLICATE_WAITING_MESSAGE, exception);
        }
    }

    private void validateRequest(WaitingCommand command, Long memberId) {
        if (!reservationRepository.existsByDateAndThemeIdAndTimeId(
                command.date(), command.themeId(), command.timeId())) {
            throw new IllegalArgumentException("예약된 일정에만 예약 대기를 신청할 수 있습니다.");
        }
        if (reservationRepository.existsByMember_IdAndDateAndTheme_IdAndTime_Id(
                memberId, command.date(), command.themeId(), command.timeId())) {
            throw new IllegalArgumentException("이미 예약한 날짜, 테마, 시간입니다.");
        }
        if (waitingRepository.existsByMember_IdAndDateAndTheme_IdAndTime_Id(
                memberId, command.date(), command.themeId(), command.timeId())) {
            throw new IllegalArgumentException(DUPLICATE_WAITING_MESSAGE);
        }
    }

    @Transactional
    public void delete(Long waitingId, LoginMember loginMember) {
        Waiting waiting = waitingRepository.findById(waitingId)
                .orElseThrow(() -> new IllegalArgumentException("예약 대기를 찾을 수 없습니다."));
        if (!waiting.getMember().getId().equals(loginMember.id())) {
            throw new AuthorizationException("예약 대기를 취소할 권한이 없습니다.");
        }
        waitingRepository.delete(waiting);
    }

    private WaitingResult toResult(Waiting waiting, long waitingNumber) {
        return new WaitingResult(
                waiting.getId(),
                waiting.getTheme().getName(),
                waiting.getDate(),
                waiting.getTime().getValue(),
                waitingNumber
        );
    }
}
