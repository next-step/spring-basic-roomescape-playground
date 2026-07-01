package roomescape.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FailedReservationService {
    private final ReservationRepository reservationRepository;

    @Autowired
    public FailedReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public void registerFailedReservation(Reservation reservation) {
        reservation.setStatus(ReservationStatus.FAILED);
        reservationRepository.save(reservation);
    }
}
