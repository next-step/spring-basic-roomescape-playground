package roomescape.waiting.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.domain.LoginMember;
import roomescape.global.exception.ApplicationException;
import roomescape.member.entity.Member;
import roomescape.member.exception.MemberErrorCode;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.dto.MyReservationResponse;
import roomescape.theme.entity.Theme;
import roomescape.theme.exception.ThemeErrorCode;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.entity.Time;
import roomescape.time.exception.TimeErrorCode;
import roomescape.time.repository.TimeRepository;
import roomescape.waiting.dto.WaitingRequest;
import roomescape.waiting.dto.WaitingResponse;
import roomescape.waiting.entity.Waiting;
import roomescape.waiting.repository.WaitingRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final MemberRepository memberRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public WaitingService(WaitingRepository waitingRepository, MemberRepository memberRepository,
                          TimeRepository timeRepository, ThemeRepository themeRepository) {
        this.waitingRepository = waitingRepository;
        this.memberRepository = memberRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public List<MyReservationResponse> findWaitingsByMember(LoginMember loginMember) {
        return waitingRepository.findWaitingsWithRankByMemberId(loginMember.id())
                .stream()
                .map(wr -> new MyReservationResponse(
                        wr.getWaiting().getId(),
                        wr.getWaiting().getTheme().getName(),
                        wr.getWaiting().getDate().toString(),
                        wr.getWaiting().getTime().getTimeValue(),
                        (wr.getRank() + 1) + "번째 예약대기"
                ))
                .toList();
    }

    @Transactional
    public WaitingResponse create(WaitingRequest request, LoginMember loginMember) {
        Member member = memberRepository.findById(loginMember.id())
                .orElseThrow(() -> new ApplicationException(MemberErrorCode.MEMBER_NOT_FOUND));
        LocalDate date = LocalDate.parse(request.getDate());
        Time time = timeRepository.findById(request.getTime())
                .orElseThrow(() -> new ApplicationException(TimeErrorCode.TIME_NOT_FOUND));
        Theme theme = themeRepository.findById(request.getTheme())
                .orElseThrow(() -> new ApplicationException(ThemeErrorCode.THEME_NOT_FOUND));

        long existingCount = waitingRepository.countByDateAndTimeAndTheme(date, time, theme);

        Waiting waiting = waitingRepository.save(
                new Waiting(date, member, time, theme)
        );

        return new WaitingResponse(waiting.getId(), existingCount + 1);
    }

    @Transactional
    public void deleteById(Long id) {
        waitingRepository.deleteById(id);
    }
}
