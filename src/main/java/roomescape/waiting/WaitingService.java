package roomescape.waiting;

import java.util.List;
import java.util.Optional;
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
        Optional<WaitingRanking> existedWaiting = waitingRepository.findAllByReservationId(
                reservation.getId());

        if (existedWaiting.isPresent()) {
            WaitingRanking waitingRanking = existedWaiting.get();
            waitingRanking.getWaiting()
                    .refreshTimestamp();
            return new WaitingRankingResponse(waitingRepository.save(waitingRanking.getWaiting()),
                    waitingRanking.getRank() + 1L);
        }

        Waiting newWaiting = new Waiting(member, reservation);
        return new WaitingRankingResponse(waitingRepository.save(newWaiting), 1L);
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
