package roomescape.waiting;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.LoginMember;
import roomescape.exception.RoomEscapeException;
import roomescape.member.MemberRepository;
import roomescape.reservation.ReservationRepository;
import roomescape.theme.ThemeRepository;
import roomescape.time.TimeRepository;

import java.util.List;

import static roomescape.exception.ErrorCode.*;

@Service
@Transactional
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final MemberRepository memberRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public WaitingService(WaitingRepository waitingRepository, MemberRepository memberRepository, TimeRepository timeRepository, ThemeRepository themeRepository, ReservationRepository reservationRepository) {
        this.waitingRepository = waitingRepository;
        this.memberRepository = memberRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    public WaitingResponse save(WaitingRequest waitingRequest, LoginMember loginMember) {
        if (waitingRequest.name() == null || waitingRequest.name().isBlank()) {
            waitingRequest = new WaitingRequest(loginMember.name(), waitingRequest.date(), waitingRequest.themeId(), waitingRequest.timeId());
        }

        validateDuplicatedReservationAndWaiting(waitingRequest, loginMember);

        Waiting waiting = new Waiting(waitingRequest.name(),
                waitingRequest.date(),
                timeRepository.findById(waitingRequest.timeId()).orElseThrow(() -> new RoomEscapeException(TIME_NOT_FOUND)),
                themeRepository.findById(waitingRequest.themeId()).orElseThrow(() -> new RoomEscapeException(THEME_NOT_FOUND)),
                memberRepository.findById(loginMember.id()).orElseThrow(() -> new RoomEscapeException(MEMBER_NOT_FOUND)));

        Waiting savedWaiting = waitingRepository.save(waiting);

        return WaitingResponse.from(savedWaiting);
    }

    public List<WaitingResponse> findWaitingWithRankByMember(LoginMember loginMember) {
        return waitingRepository.findWaitingWithRankByMemberId(loginMember.id()).stream()
                .map(WaitingResponse::from)
                .toList();
    }

    public void deleteById(Long id) {
        waitingRepository.deleteById(id);
    }

    private void validateDuplicatedReservationAndWaiting(WaitingRequest waitingRequest, LoginMember loginMember) {
        boolean alreadyReserved = reservationRepository.existsByMemberIdAndThemeIdAndDateAndTimeId(
                loginMember.id(), waitingRequest.themeId(), waitingRequest.date(), waitingRequest.timeId());
        if (alreadyReserved) {
            throw new RoomEscapeException(DUPLICATE_RESERVATION);
        }

        boolean alreadyWaiting = waitingRepository.existsByMemberIdAndThemeIdAndDateAndTimeId(
                loginMember.id(), waitingRequest.themeId(), waitingRequest.date(), waitingRequest.timeId());
        if (alreadyWaiting) {
            throw new RoomEscapeException(DUPLICATE_WAITING);
        }
    }
}
