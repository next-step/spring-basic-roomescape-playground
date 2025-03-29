package roomescape.waiting;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import roomescape.auth.domain.LoginMember;
import roomescape.error.ErrorMessage;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationRepository;

@Service
@Transactional
public class WaitingService {
    private final WaitingRepository waitingRepository;
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;

    public WaitingService(WaitingRepository waitingRepository, MemberRepository memberRepository,
                          ReservationRepository reservationRepository) {
        this.waitingRepository = waitingRepository;
        this.memberRepository = memberRepository;
        this.reservationRepository = reservationRepository;
    }

    public WaitingResponse createWaiting(WaitingRequest waitingRequest, LoginMember loginMember) {
        Member member = findMemberById(loginMember);
        Reservation reservation = findReservation(waitingRequest);
        Waiting waiting = new Waiting(reservation.getDate(), reservation.getTime().getValue(), reservation.getTheme(), member, reservation);

        validateWaiting(waiting);
        return saveWaiting(waiting);
    }

    private void validateWaiting(Waiting waiting) {
        if (waiting.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(ErrorMessage.WAITING_MUST_AFTER_NOW.getMessage());
        }
    }

    public void deleteWaiting(Long id) {
        waitingRepository.deleteById(id);
    }

    private Member findMemberById(LoginMember loginMember) {
        return memberRepository.findById(loginMember.id())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.MEMBER_NOT_FOUND.getMessage()));
    }

    private Reservation findReservation(WaitingRequest waitingRequest) {
        return reservationRepository.findByDateAndTimeIdAndThemeId(waitingRequest.getDate(), waitingRequest.getTime(), waitingRequest.getTheme());
    }

    private WaitingResponse saveWaiting(Waiting waiting) {
        waitingRepository.save(waiting);

        Long id = waiting.getTheme().getId();
        String time = waiting.getTime();
        String date = waiting.getDate();

        return new WaitingResponse(waiting.getId(), id, date, time, waitingRepository.findByThemeIdAndDateAndTime(id, date, time).size());
    }
}
