package roomescape.waiting;

import java.util.Optional;
import org.springframework.stereotype.Service;
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

    public WaitingResponse create(Member member, WaitingRequest request) {
        Reservation reservation = reservationRepository.findByDateAndReservationTime_IdAndTheme_Id(
                        request.date(), request.time(), request.theme())
                .orElseThrow(
                        () -> new RoomescapeNotFoundException("예약이 존재하지 않습니다. 대기 대신 예약을 해주세요."));
        Optional<Waiting> existedWaiting = waitingRepository.findByMemberId(member.getId());

        if (existedWaiting.isPresent()) {
            existedWaiting.get().refreshTimestamp();
            return new WaitingResponse(waitingRepository.save(existedWaiting.get()));
        }

        Waiting newWaiting = new Waiting(member, reservation);
        return new WaitingResponse(waitingRepository.save(newWaiting));
    }
}
