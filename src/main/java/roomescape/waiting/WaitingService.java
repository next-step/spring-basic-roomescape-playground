package roomescape.waiting;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.dto.LoginMember;
import roomescape.auth.exception.UnauthenticatedException;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.reservation.ReservationRepository;
import roomescape.theme.ThemeRepository;
import roomescape.time.TimeRepository;
import roomescape.waiting.dto.WaitingRequest;
import roomescape.waiting.dto.WaitingResponse;

import java.time.LocalDate;

@Service
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;

    public WaitingService(WaitingRepository waitingRepository, ReservationRepository reservationRepository, MemberRepository memberRepository, ThemeRepository themeRepository, TimeRepository timeRepository) {
        this.waitingRepository = waitingRepository;
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
    }

    @Transactional
    public WaitingResponse create(WaitingRequest request, LoginMember loginMember) {
        if (loginMember == null) {
            throw new UnauthenticatedException("로그인이 필요합니다.");
        }
        Member member = memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

        validateNoDuplicate(request.getTheme(), request.getDate(), request.getTime(), member.getId());

        Waiting waiting = new Waiting(member, request.getTheme(), request.getTime(), request.getDate());
        Waiting savedWaiting = waitingRepository.save(waiting);
        return WaitingResponse.from(savedWaiting);
    }

    private void validateNoDuplicate(Long themeId, LocalDate date, Long timeId, Long memberId) {
        if (reservationRepository.existsByThemeIdAndDateAndTimeIdAndMember_Id(themeId, date, timeId, memberId)) {
            throw new IllegalArgumentException("이미 해당 시간에 예약이 존재합니다.");
        }
        if (waitingRepository.existsByThemeIdAndDateAndTimeIdAndMember_Id(themeId, date, timeId, memberId)) {
            throw new IllegalArgumentException("이미 해당 시간에 예약 대기 중입니다.");
        }
    }

    @Transactional
    public void delete(Long waitingId, LoginMember loginMember) {
        if (loginMember == null) {
            throw new UnauthenticatedException("로그인이 필요합니다.");
        }
        Waiting waiting = waitingRepository.findById(waitingId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 대기입니다."));

        if (!waiting.getMember().getId().equals(loginMember.getId())) {
            throw new UnauthenticatedException("자신의 예약 대기만 취소할 수 있습니다.");
        }
        waitingRepository.delete(waiting);
    }
}
