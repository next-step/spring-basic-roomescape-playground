package roomescape.reservation.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import roomescape.auth.domain.LoginMember;
import roomescape.reservation.Status;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.waiting.Waiting;
import roomescape.waiting.WaitingRepository;

@Service
public class ReservationFindService {
    private final ReservationRepository reservationRepository;
    private final WaitingRepository waitingRepository;

    public ReservationFindService(ReservationRepository reservationRepository, WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.waitingRepository = waitingRepository;
    }

    public List<ReservationResponse> findReservations(LoginMember loginMember) {
        boolean isAdmin = loginMember.isAdmin();

        List<ReservationResponse> reservations;
        List<ReservationResponse> waitings;

        if (isAdmin) {
            reservations = findAllReservations();
            waitings = findAllWaitings();
        } else {
            reservations = findMemberReservations(loginMember);
            waitings = findMemberWaitings(loginMember);
        }

        return mergeAndSortByDate(reservations, waitings);
    }

    private List<ReservationResponse> findAllReservations() {
        return reservationRepository.findAll().stream()
                .map(reservation -> ReservationResponse.from(reservation, Status.RESERVATION))
                .toList();
    }

    private List<ReservationResponse> findAllWaitings() {
        return waitingRepository.findAll().stream()
                .map(waiting -> ReservationResponse.from(waiting.getReservation(), Status.WAIT))
                .toList();
    }

    private List<ReservationResponse> findMemberReservations(LoginMember loginMember) {
        return reservationRepository.findAllByMemberId(loginMember.id()).stream()
                .map(reservation -> ReservationResponse.from(reservation, Status.RESERVATION.getDescription()))
                .toList();
    }

    private List<ReservationResponse> findMemberWaitings(LoginMember loginMember) {
        return waitingRepository.findWaitingsWithRankByMemberId(loginMember.id()).stream()
                .map(waitingWithRank -> {
                    Waiting waiting = waitingWithRank.getWaiting();
                    String status = (waitingWithRank.getRank() + 1) + "번째 " + Status.WAIT.getDescription();
                    return ReservationResponse.from(waiting, status);
                })
                .toList();
    }

    private List<ReservationResponse> mergeAndSortByDate(List<ReservationResponse> reservations,
                                                         List<ReservationResponse> waitings) {
        return Stream.concat(reservations.stream(), waitings.stream())
                .sorted(Comparator.comparing(ReservationResponse::getDate))
                .toList();
    }
}
