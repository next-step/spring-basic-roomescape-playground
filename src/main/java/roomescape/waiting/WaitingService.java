package roomescape.waiting;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.auth.AuthClaims;
import roomescape.exception.MemberNotFoundException;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
@RequiredArgsConstructor
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public WaitingResponse save(WaitingRequest waitingRequest, AuthClaims authClaims){
        Time time = timeRepository.findById(waitingRequest.time())
                .orElseThrow(() -> new IllegalArgumentException("해당 시간이 존재하지 않습니다."));
        Theme theme = themeRepository.findById(waitingRequest.theme())
                .orElseThrow(() -> new IllegalArgumentException("해당 테마가 존재하지 않습니다."));
        Member member = findMemberByRole(waitingRequest, authClaims);

        Waiting waiting = new Waiting(waitingRequest.name(),waitingRequest.date(), time, theme, member);
        waitingRepository.save(waiting);

        return new WaitingResponse(waiting.getId(), waiting.getName(), waiting.getDate(), waiting.getTime().getTime(), waiting.getTheme().getName());
    }

    private Member findMemberByRole(WaitingRequest request, AuthClaims claims) {
        if ("ADMIN".equals(claims.role()) && request.name() != null) { // 관리자일 경우 name 조회
            return memberRepository.findByName(request.name())
                    .orElseThrow(() -> new MemberNotFoundException("해당 사용자가 존재하지 않습니다."));
        }
        return memberRepository.findById(claims.id()) // 사용자일 경우 id 조회
                .orElseThrow(() -> new MemberNotFoundException("해당 사용자가 존재하지 않습니다."));
    }

    public void deleteById(final Long id) {
        waitingRepository.deleteById(id);
    }
}
