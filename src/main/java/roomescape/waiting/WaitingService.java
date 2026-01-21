package roomescape.waiting;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.ErrorMessage;
import roomescape.exception.ForbiddenException;
import roomescape.exception.NotFoundDataException;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberService;
import roomescape.reservation.ReservationValidator;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class WaitingService {
    private final WaitingRepository waitingRepository;
    private final MemberService memberService;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final ReservationValidator reservationValidator;

    public WaitingService(WaitingRepository waitingRepository,
            MemberService memberService,
            TimeRepository timeRepository,
            ThemeRepository themeRepository,
            ReservationValidator reservationValidator) {
        this.waitingRepository = waitingRepository;
        this.memberService = memberService;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.reservationValidator = reservationValidator;
    }

    @Transactional
    public WaitingResponse save(WaitingRequest waitingRequest, LoginMember loginMember) {
        Member member = memberService.findById(loginMember.id());

        Time time = timeRepository.findById(waitingRequest.time())
                                  .orElseThrow(() -> new NotFoundDataException(ErrorMessage.TIME_NOT_FOUND.getMessage()));

        Theme theme = themeRepository.findById(waitingRequest.theme())
                                     .orElseThrow(() -> new NotFoundDataException(ErrorMessage.THEME_NOT_FOUND.getMessage()));

        reservationValidator.validateWaitingCreation(member.getId(), waitingRequest.date(), time.getId(), theme.getId());

        long count = waitingRepository.countByDateAndTimeIdAndThemeId(
                waitingRequest.date(),
                time.getId(),
                theme.getId()
        );
        long rank = count + 1;

        Waiting waiting = new Waiting(waitingRequest.date(), time, theme, member);
        waitingRepository.save(waiting);

        return new WaitingResponse(
                waiting.getId(),
                member.getName(),
                theme.getName(),
                waiting.getDate(),
                time.getValue(),
                rank + "번째 예약대기"
        );
    }

    @Transactional
    public void deleteById(Long id, LoginMember loginMember) {
        Waiting waiting = waitingRepository.findByIdWithMember(id)
                                           .orElseThrow(() -> new NotFoundDataException(ErrorMessage.WAITING_NOT_FOUND.getMessage()));

        if (!waiting.getMember().getId().equals(loginMember.id())) {
            throw new ForbiddenException(ErrorMessage.ONLY_OWN_WAITING_CAN_BE_CANCELLED.getMessage());
        }

        waitingRepository.deleteById(id);
    }

    public List<WaitingWithRank> findWaitingsWithRankByMemberId(Long memberId) {
        return waitingRepository.findWaitingsWithRankByMemberId(memberId);
    }
}
