package roomescape.waiting.service;

import org.springframework.stereotype.Service;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRepository;
import roomescape.reservation.controller.dto.ReservationRequest;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;
import roomescape.time.domain.Time;
import roomescape.time.domain.TimeRepository;
import roomescape.waiting.controller.dto.WaitingWithRank;
import roomescape.waiting.domain.Waiting;
import roomescape.waiting.domain.WaitingRepository;

@Service
public class WaitingService {
    private final WaitingRepository waitingRepository;
    private final MemberRepository memberRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;

    public WaitingService(WaitingRepository waitingRepository,
                          MemberRepository memberRepository,
                          ThemeRepository themeRepository,
                          TimeRepository timeRepository) {
        this.waitingRepository = waitingRepository;
        this.memberRepository = memberRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
    }

    public WaitingWithRank waiting(String memberName,
                                   ReservationRequest reservationRequest) {
        Waiting waiting = waitingRepository.save(requestToWaiting(memberName, reservationRequest));
        long rank = waitingRepository.countWaitingBy(waiting.getTheme(), waiting.getTime(), waiting.getDate(), waiting.getId()) + 1;
        return new WaitingWithRank(waiting, rank);
    }

    public void cancel(String memberName, Long waitingId) {
        boolean isValid = waitingRepository.existsByMemberNameAndId(memberName, waitingId);
        if (isValid) {
            waitingRepository.deleteById(waitingId);
            return;
        }
        throw new RuntimeException("유효한 예약만 취소할 수 있습니다,");
    }

    private Waiting requestToWaiting(String memberName, ReservationRequest reservationRequest) {
        Member member = findMemberByName(memberName);
        Theme themeProxy = findThemeProxyById(reservationRequest.theme());
        Time timeProxy = findTimeProxyById(reservationRequest.time());
        return new Waiting(memberName, reservationRequest.date(), member, timeProxy, themeProxy);
    }

    private Member findMemberByName(String name) {
        return memberRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 멤버입니다."));
    }

    private Theme findThemeProxyById(Long id) {
        return themeRepository.getReferenceById(id);
    }

    private Time findTimeProxyById(Long id) {
        return timeRepository.getReferenceById(id);
    }
}
