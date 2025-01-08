package roomescape.reservation;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {
    private ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Reservation reservation = reservationRepository.save(new Reservation(
                reservationRequest.getName(),
                reservationRequest.getDate(),
                reservationRequest.getTime(),
                reservationRequest.getTheme()));

        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getTheme(),
                reservation.getDate(),
                reservation.getTime());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme(), it.getDate(), it.getTime()))
                .toList();
    }
}
