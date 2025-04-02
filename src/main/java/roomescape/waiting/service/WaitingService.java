package roomescape.waiting.service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import roomescape.auth.domain.LoginMember;
import roomescape.error.ErrorMessage;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.waiting.domain.Waiting;
import roomescape.waiting.dto.WaitingRequest;
import roomescape.waiting.dto.WaitingResponse;
import roomescape.waiting.repository.WaitingRepository;

@Service
@Transactional
public class WaitingService {
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;
    private final WaitingRepository waitingRepository;

    public WaitingService(WaitingRepository waitingRepository, MemberRepository memberRepository,
                          ReservationRepository reservationRepository) {
        this.waitingRepository = waitingRepository;
        this.memberRepository = memberRepository;
        this.reservationRepository = reservationRepository;
    }

    public WaitingResponse createWaiting(WaitingRequest waitingRequest, LoginMember loginMember) {
        Member member = findMember(loginMember);
        Reservation reservation = findReservation(waitingRequest);

        validateWaitingConditions(loginMember, reservation, member);

        Waiting waiting = new Waiting(reservation.getDate(), reservation.getTime().getValue(), reservation.getTheme(), member, reservation);
        return saveWaiting(waiting);
    }

    public void deleteWaiting(Long id) {
        waitingRepository.deleteById(id);
    }

    private Member findMember(LoginMember loginMember) {
        return memberRepository.findByEmailAndName(loginMember.email(), loginMember.name())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.MEMBER_NOT_FOUND.getMessage()));
    }

    private Reservation findReservation(WaitingRequest waitingRequest) {
        return reservationRepository.findByDateAndTimeIdAndThemeId(
                        waitingRequest.getDate(),
                        waitingRequest.getTime(),
                        waitingRequest.getTheme()
                ).orElseThrow(() -> new IllegalArgumentException(ErrorMessage.RESERVATION_NOT_FOUND.getMessage()));
    }

    private void validateWaitingConditions(LoginMember loginMember, Reservation reservation, Member member) {
        Reservation savedReservation = reservationRepository.findByDateAndTimeIdAndThemeId(
                reservation.getDate(),
                reservation.getTime().getId(),
                reservation.getTheme().getId()
        ).orElseThrow(() -> new IllegalArgumentException(ErrorMessage.RESERVATION_NOT_FOUND.getMessage()));

        if (savedReservation.isSavedSameMember(member)) {
            throw new IllegalArgumentException(ErrorMessage.ALREADY_RESERVATION.getMessage());
        }

        if (isAlreadyInWaiting(loginMember, reservation)) {
            throw new IllegalArgumentException(ErrorMessage.ALREADY_WAITING.getMessage());
        }
    }

    private boolean isAlreadyInWaiting(LoginMember loginMember, Reservation reservation) {
        return waitingRepository.existsByMemberEmailAndDateAndTimeAndThemeId(
                loginMember.email(),
                reservation.getDate(),
                reservation.getTime().getValue(),
                reservation.getTheme().getId()
        );
    }

    private WaitingResponse saveWaiting(Waiting waiting) {
        validateWaiting(waiting);
        waitingRepository.save(waiting);

        Long id = waiting.getTheme().getId();
        String time = waiting.getTime();
        String date = waiting.getDate();

        return new WaitingResponse(waiting.getId(), id, date, time,
                waitingRepository.findAllByThemeIdAndDateAndTime(id, date, time).size());
    }

    private void validateWaiting(Waiting waiting) {
        if (waiting.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(ErrorMessage.WAITING_MUST_AFTER_NOW.getMessage());
        }
    }
}
