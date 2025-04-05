package roomescape.waiting;

import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.global.exception.RoomescapeBadRequestException;
import roomescape.global.exception.RoomescapeNotFoundException;
import roomescape.member.Member;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationRepository;

@Service
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final ReservationRepository reservationRepository;

    public WaitingService(WaitingRepository waitingRepository,
                          ReservationRepository reservationRepository) {
        this.waitingRepository = waitingRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public WaitingRankingResponse create(Member member, WaitingRequest request) {
        Reservation reservation = getReservation(member, request);
        List<Waiting> waitings = getWaitings(member, reservation);

        Waiting newWaiting = new Waiting(member, reservation);
        return new WaitingRankingResponse(waitingRepository.save(newWaiting), waitings.size());
    }

    private List<Waiting> getWaitings(Member member, Reservation reservation) {
        List<Waiting> waitings = waitingRepository.findAllByReservationId(
                reservation.getId());

        waitings.stream()
                .filter(waiting -> waiting.isOwner(member.getId()))
                .findAny()
                .ifPresent(w -> { throw new RoomescapeBadRequestException("이미 해당 예약에 대기를 하셨습니다."); });

        return waitings;
    }

    private Reservation getReservation(Member member, WaitingRequest request) {
        Reservation reservation = reservationRepository.findByDateAndReservationTime_IdAndTheme_Id(
                        request.date(), request.time(), request.theme())
                .orElseThrow(
                        () -> new RoomescapeNotFoundException("예약이 존재하지 않습니다. 대기 대신 예약을 해주세요."));

        if (reservation.isMadeByAdmin()) {
            return reservation;
        }
        if (reservation.isOwner(member)) {
            throw new RoomescapeBadRequestException("본인이 예약한 방에는 대기를 할 수 없습니다.");
        }
        return reservation;
    }

    public List<WaitingRankingResponse> getMemberWaitings(Member member) {
        List<WaitingRanking> waitingRankings = waitingRepository.findWaitingRankingByMemberId(
                member.getId());

        return waitingRankings.stream()
                .map(waitingRanking -> new WaitingRankingResponse(waitingRanking.getWaiting(),
                        waitingRanking.getRank()))
                .toList();
    }

    @Transactional
    public void deleteById(long reservationId, long memberId) {
        waitingRepository.deleteByReservation_IdAndMember_Id(reservationId, memberId);
    }
}
