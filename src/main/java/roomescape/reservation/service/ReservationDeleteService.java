package roomescape.reservation.service;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import roomescape.auth.domain.LoginMember;
import roomescape.error.ErrorMessage;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.waiting.Waiting;
import roomescape.waiting.WaitingRepository;

@Service
@Transactional
public class ReservationDeleteService {
    private final ReservationRepository reservationRepository;
    private final WaitingRepository waitingRepository;

    public ReservationDeleteService(ReservationRepository reservationRepository, WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.waitingRepository = waitingRepository;
    }

    public void deleteReservation(Long id, LoginMember loginMember) {
        Reservation reservation = findReservationById(id);

        validateReservationPermission(reservation, loginMember);
        deleteReservation(reservation);
    }

    private Reservation findReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.RESERVATION_NOT_FOUND.getMessage()));
    }

    private void validateReservationPermission(Reservation reservation, LoginMember loginMember) {
        if (loginMember.notHaveName(reservation.getMember().getName()) && loginMember.isNotAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ErrorMessage.FORBIDDEN_RESERVATION.getMessage());
        }
    }

    private void deleteReservation(Reservation reservation) {
        if (reservation.remainWaitings()) {
            Waiting waiting = waitingRepository.findTopByReservationOrderByCreatedDateTime(reservation)
                    .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.WAITING_NOT_FOUND.getMessage()));
            waitingRepository.delete(waiting);
            waiting.changeToReservation();
        } else {
            reservationRepository.deleteById(reservation.getId());
        }
    }
}
