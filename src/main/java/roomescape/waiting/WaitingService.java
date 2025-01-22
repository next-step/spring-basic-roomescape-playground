package roomescape.waiting;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import roomescape.authentication.MemberAuthInfo;
import roomescape.exception.MemberNotFoundException;
import roomescape.exception.ThemeNotFoundException;
import roomescape.exception.TimeNotFoundException;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final MemberRepository memberRepository;

    public WaitingResponse createWaiting(WaitingRequest waitingRequest, MemberAuthInfo memberAuthInfo) {
        validateWaitingRequest(waitingRequest);

        if (waitingRequest.name() == null){
            waitingRequest = new WaitingRequest(
                    memberAuthInfo.name(),
                    waitingRequest.date(),
                    waitingRequest.theme(),
                    waitingRequest.time());
        }

        Theme theme = themeRepository.findById(waitingRequest.theme())
                .orElseThrow(() -> new ThemeNotFoundException("해당 테마를 찾을 수 없습니다."));
        Time time = timeRepository.findById(waitingRequest.time())
                .orElseThrow(() -> new TimeNotFoundException("예약 시간을 찾을 수 없습니다."));
        Member member = null;
        if (memberAuthInfo.id() != null){ //관리자가 아닌, 사용자일 경우
            member = memberRepository.findById(memberAuthInfo.id())
                    .orElseThrow(() -> new MemberNotFoundException("가입된 회원이 아닙니다."));
        }

        Waiting waiting = new Waiting(
                member, waitingRequest.name(), waitingRequest.date(), time, theme
        );

        waitingRepository.save(waiting);

        List<Waiting> waitings = waitingRepository.findByDateAndTime_IdAndTheme_Id(waiting.getDate(), time.getId(), theme.getId());

        return new WaitingResponse(
                waiting.getId(),
                waiting.getName(),
                waiting.getTheme().getName(),
                waiting.getDate(),
                waiting.getTime().getValue(),
                (long) waitings.size());
    }

    public void cancelWaiting(Long id) {
        waitingRepository.deleteById(id);
    }

    private void validateWaitingRequest(WaitingRequest waitingRequest) {
        if (waitingRequest.date() == null || waitingRequest.theme() == null || waitingRequest.time() == null) {
            throw new IllegalArgumentException("유효하지 않은 요청입니다. 필요한 값이 누락되었습니다.");
        }
    }
}
