package roomescape.waiting;

import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import roomescape.auth.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberService;
import roomescape.reservation.ReservationRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.time.LocalDateTime;

@Service
@Transactional
public class WaitingService {
    private final WaitingRepository waitingRepository;
    private final MemberService memberService;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final ReservationRepository reservationRepository;

    public WaitingService(WaitingRepository waitingRepository, MemberService memberService, ThemeRepository themeRepository, TimeRepository timeRepository, ReservationRepository reservationRepository) {
        this.waitingRepository = waitingRepository;
        this.memberService = memberService;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
    }

    public WaitingResponse save(WaitingRequest waitingRequest, LoginMember loginMember) {

        Member member = memberService.findById(loginMember.id());

        validateWaiting(waitingRequest, member);

        Theme theme = themeRepository.findById(waitingRequest.theme())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));

        Time time = timeRepository.findById(waitingRequest.time())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));

        Waiting waiting = new Waiting(member.getName(), waitingRequest.date(), time, theme, member, LocalDateTime.now());

        try {
            waitingRepository.save(waiting);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("이미 예약 대기 중입니다.");
        }

        return WaitingResponse.from(waiting);
    }

    public void deleteById(Long id) {
        waitingRepository.deleteById(id);
    }

    private void validateWaiting(WaitingRequest request, Member member) {

        if (!reservationRepository.existsByDateAndTimeIdAndThemeId(
                request.date(),
                request.time(),
                request.theme())) {
            throw new IllegalArgumentException("예약이 존재하지 않아 대기할 수 없습니다.");
        }

        if (waitingRepository.existsByMemberIdAndDateAndTimeIdAndThemeId(
                member.getId(),
                request.date(),
                request.time(),
                request.theme())) {
            throw new IllegalArgumentException("이미 예약 대기 중입니다.");
        }

        if (reservationRepository.existsByMemberIdAndDateAndTimeIdAndThemeId(
                member.getId(),
                request.date(),
                request.time(),
                request.theme())) {
            throw new IllegalArgumentException("본인이 예약한 일정은 예약 대기할 수 없습니다.");
        }
    }
}
