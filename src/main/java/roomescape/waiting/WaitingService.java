package roomescape.waiting;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.reservation.ReservationRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
@Transactional(readOnly = true)
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final MemberRepository memberRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public WaitingService(WaitingRepository waitingRepository, MemberRepository memberRepository,
                          TimeRepository timeRepository, ThemeRepository themeRepository,
                          ReservationRepository reservationRepository) {
        this.waitingRepository = waitingRepository;
        this.memberRepository = memberRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public WaitingResponse save(WaitingRequest request, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(IllegalArgumentException::new);
        Time time = timeRepository.findById(request.getTime()).orElseThrow(IllegalArgumentException::new);
        Theme theme = themeRepository.findById(request.getTheme()).orElseThrow(IllegalArgumentException::new);

        reservationRepository.findByDateAndThemeIdAndTimeId(request.getDate(), request.getTheme(), request.getTime())
                .filter(r -> r.getMember() != null && r.getMember().getId().equals(memberId))
                .ifPresent(r -> { throw new IllegalArgumentException(); });

        waitingRepository.findByDateAndThemeIdAndTimeIdAndMemberId(request.getDate(), request.getTheme(), request.getTime(), memberId)
                .ifPresent(w -> { throw new IllegalArgumentException(); });

        Waiting waiting = new Waiting(request.getDate(), member, time, theme);
        Waiting saved = waitingRepository.save(waiting);
        return new WaitingResponse(saved.getId());
    }

    @Transactional
    public void deleteById(Long id) {
        waitingRepository.deleteById(id);
    }
}
