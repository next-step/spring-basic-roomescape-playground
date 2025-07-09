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
        validateDuplicatedReservationAndWaiting(waitingRequest, loginMember);

        Waiting waiting = toWaiting(waitingRequest, loginMember);
        Waiting savedWaiting = waitingRepository.save(waiting);
        Long waitingNumber = waitingRepository.getWaitingRank(
                waiting.getTheme(), waiting.getDate(), waiting.getTime(), waiting.getId());
        return WaitingResponse.from(savedWaiting, waitingNumber);
    }

    public List<WaitingResponse> findWaitingWithRankByMember(LoginMember loginMember) {
        return waitingRepository.findWaitingWithRankByMemberId(loginMember.id()).stream()
                .map(WaitingResponse::from)
                .toList();
    }

    public void deleteById(Long id) {
        Waiting waiting = waitingRepository.findById(id)
                .orElseThrow(() -> new RoomEscapeException(WAITING_NOT_FOUND));
        waitingRepository.delete(waiting);
    }


    private Waiting toWaiting(WaitingRequest waitingRequest, LoginMember loginMember) {
        Waiting waiting = new Waiting(loginMember.name(),
                waitingRequest.date(),
                timeRepository.findById(waitingRequest.time()).orElseThrow(() -> new RoomEscapeException(TIME_NOT_FOUND)),
                themeRepository.findById(waitingRequest.theme()).orElseThrow(() -> new RoomEscapeException(THEME_NOT_FOUND)),
                memberRepository.findById(loginMember.id()).orElseThrow(() -> new RoomEscapeException(MEMBER_NOT_FOUND)));
        return waiting;
    }

    private void validateDuplicatedReservationAndWaiting(WaitingRequest waitingRequest, LoginMember loginMember) {
        boolean alreadyReserved = reservationRepository.existsByMemberIdAndThemeIdAndDateAndTimeId(
                loginMember.id(), waitingRequest.theme(), waitingRequest.date(), waitingRequest.time());
        if (alreadyReserved) {
            throw new RoomEscapeException(DUPLICATE_RESERVATION);
        }

        boolean alreadyWaiting = waitingRepository.existsByMemberIdAndThemeIdAndDateAndTimeId(
                loginMember.id(), waitingRequest.theme(), waitingRequest.date(), waitingRequest.time());
        if (alreadyWaiting) {
            throw new RoomEscapeException(DUPLICATE_WAITING);
        }
    }
}
