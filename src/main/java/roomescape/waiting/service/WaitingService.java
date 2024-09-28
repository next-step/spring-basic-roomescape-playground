package roomescape.waiting.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import roomescape.member.dto.LoginMember;
import roomescape.member.model.Member;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.dto.MyReservationResponse;
import roomescape.theme.model.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.model.Time;
import roomescape.time.repository.TimeRepository;
import roomescape.waiting.dto.WaitingRequest;
import roomescape.waiting.dto.WaitingResponse;
import roomescape.waiting.model.Waiting;
import roomescape.waiting.model.WaitingWithRank;
import roomescape.waiting.repository.WaitingRepository;

@Service
@RequiredArgsConstructor
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final MemberRepository memberRepository;

    public WaitingResponse save(WaitingRequest request, LoginMember loginMember) {
        String name = Optional.ofNullable(request.name()).orElse(loginMember.name());
        Theme theme = themeRepository.getById(request.theme());
        Time time = timeRepository.getById(request.time());
        Member member = memberRepository.getById(loginMember.id());
        Waiting waiting = waitingRepository.save(
            Waiting.builder()
                .name(name)
                .date(request.date())
                .theme(theme)
                .time(time)
                .member(member)
                .build()
        );
        WaitingWithRank waitingWithRank = waitingRepository.getWaitingWithRankById(waiting.getId());
        return WaitingResponse.from(waitingWithRank);
    }

    public void deleteById(Long id) {
        waitingRepository.deleteById(id);
    }

    public List<MyReservationResponse> findMyReservations(LoginMember loginMember) {
        Member member = memberRepository.getById(loginMember.id());
        return waitingRepository.findWaitingsWithRankByMemberId(member.getId()).stream()
            .map(MyReservationResponse::from)
            .toList();
    }
}
