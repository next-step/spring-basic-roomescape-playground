package roomescape.reservation.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import roomescape.auth.domain.LoginMember;
import roomescape.reservation.Status;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.waiting.domain.Waiting;
import roomescape.waiting.repository.WaitingRepository;

@Service
public class ReservationFindService {
    private final ReservationRepository reservationRepository;
    private final WaitingRepository waitingRepository;

    public ReservationFindService(ReservationRepository reservationRepository, WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.waitingRepository = waitingRepository;
    }

    public List<ReservationResponse> findReservations(LoginMember loginMember) {
        if (loginMember.isAdmin()) {
            return findReservationsForAdmin();
        }
        return findReservationsForMember(loginMember);
    }

    private List<ReservationResponse> findReservationsForAdmin() {
        List<ReservationResponse> reservations = findAllReservations();
        List<ReservationResponse> waitings = findAllWaitings();
        return mergeAndSortByDate(reservations, waitings);
    }

    private List<ReservationResponse> findReservationsForMember(LoginMember loginMember) {
        List<ReservationResponse> reservations = findMemberReservations(loginMember);
        List<ReservationResponse> waitings = findMemberWaitings(loginMember);
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
        return waitingRepository.findRankedWaitingsByMemberId(loginMember.id()).stream()
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
