package roomescape.waiting.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import roomescape.auth.LoginMember;
import roomescape.exception.*;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.Time;
import roomescape.time.repository.TimeRepository;
import roomescape.waiting.domain.Waiting;
import roomescape.waiting.dto.WaitingRequest;
import roomescape.waiting.dto.WaitingResponse;
import roomescape.waiting.repository.WaitingRepository;

@Service
public class WaitingService {
    private final WaitingRepository waitingRepository;
    private final MemberRepository memberRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public WaitingService(WaitingRepository waitingRepository, MemberRepository memberRepository, TimeRepository timeRepository, ThemeRepository themeRepository) {
        this.waitingRepository = waitingRepository;
        this.memberRepository = memberRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public WaitingResponse save(LoginMember loginMember, WaitingRequest waitingRequest) {
        Member member = memberRepository.findById(loginMember.id())
                .orElseThrow(() -> new NotFoundMemberException("예약 대기할 회원을 찾을 수 없습니다."));

        Time time = timeRepository.findByIdAndDeletedFalse(waitingRequest.time())
                .orElseThrow(() -> new NotFoundTimeException("예약 대기 시간을 찾을 수 없습니다."));

        Theme theme = themeRepository.findByIdAndDeletedFalse(waitingRequest.theme())
                .orElseThrow(() -> new NotFoundThemeException("예약 대기 테마를 찾을 수 없습니다."));

        Waiting waiting;

        try {
            waiting = waitingRepository.save(new Waiting(member, theme, waitingRequest.date(), time));
        } catch (DataIntegrityViolationException exception) {
            throw new DuplicateWaitingException("이미 신청한 예약 대기입니다.");
        }

        long waitingNumber = waitingRepository.countByDateAndThemeIdAndTimeId(
                waitingRequest.date(),
                waitingRequest.theme(),
                waitingRequest.time()
        );

        return new WaitingResponse(waiting.getId(), waitingNumber);
    }

    public void deleteById(Long id) {
        Waiting waiting = waitingRepository.findById(id)
                .orElseThrow(() -> new NotFoundWaitingException("취소할 대기중인 예약을 찾을 수 없습니다."));

        waitingRepository.delete(waiting);
    }
}
