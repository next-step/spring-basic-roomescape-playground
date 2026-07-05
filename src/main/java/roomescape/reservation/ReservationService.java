package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.member.DTO.MemberResponse;
import roomescape.reservation.DTO.ReservationRequest;
import roomescape.reservation.DTO.ReservationResponse;

import java.util.List;

@Service
public class ReservationService {
    private ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, MemberResponse member) {
        String reservatorName = reservationRequest.getName() != null ? reservationRequest.getName() : member.getName();

        ReservationRequest newReservation = new ReservationRequest(reservatorName, reservationRequest.getDate(), reservationRequest.getTime(), reservationRequest.getTheme());
        Reservation reservation = reservationRepository.save(newReservation);

        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }
}
