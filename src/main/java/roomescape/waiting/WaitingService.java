package roomescape.waiting;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.ForbiddenException;
import roomescape.exception.InvalidDataException;
import roomescape.exception.NotFoundDataException;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
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
    private final MemberRepository memberRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final ReservationValidator reservationValidator;

    public WaitingService(WaitingRepository waitingRepository,
                          MemberRepository memberRepository,
                          TimeRepository timeRepository,
                          ThemeRepository themeRepository,
                          ReservationValidator reservationValidator) {
        this.waitingRepository = waitingRepository;
        this.memberRepository = memberRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.reservationValidator = reservationValidator;
    }

    @Transactional
    public WaitingResponse save(WaitingRequest waitingRequest, LoginMember loginMember) {
        Member member = memberRepository.findById(loginMember.id());

        Time time = timeRepository.findById(waitingRequest.getTime())
                .orElseThrow(() -> new NotFoundDataException("해당 시간을 찾을 수 없습니다."));

        Theme theme = themeRepository.findById(waitingRequest.getTheme())
                .orElseThrow(() -> new NotFoundDataException("해당 테마를 찾을 수 없습니다."));

        reservationValidator.validateWaitingCreation(member.getId(), waitingRequest.getDate(), time.getId(), theme.getId());

        long count = waitingRepository.countByDateAndTimeIdAndThemeId(
                waitingRequest.getDate(),
                time.getId(),
                theme.getId()
        );
        long rank = count + 1;

        Waiting waiting = new Waiting(waitingRequest.getDate(), time, theme, member);
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
        Waiting waiting = waitingRepository.findById(id)
                                           .orElseThrow(() -> new NotFoundDataException("해당 대기를 찾을 수 없습니다."));

        if (!waiting.getMember().getId().equals(loginMember.id())) {
            throw new ForbiddenException("본인의 대기만 취소할 수 있습니다.");
        }

        waitingRepository.deleteById(id);
    }

    public List<WaitingWithRank> findWaitingsWithRankByMemberId(Long memberId) {
        return waitingRepository.findWaitingsWithRankByMemberId(memberId);
    }
}
