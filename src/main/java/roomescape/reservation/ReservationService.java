package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.JwtUtil;
import roomescape.waiting.WaitingRepository;
import roomescape.waiting.WaitingWithRank;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final WaitingRepository waitingRepository;

    public ReservationService(ReservationDao reservationDao, WaitingRepository waitingRepository) {
        this.reservationDao = reservationDao;
        this.waitingRepository = waitingRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Reservation reservation = reservationDao.save(reservationRequest);
        return new ReservationResponse(reservation.getId(), reservationRequest.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .collect(Collectors.toList());
    }

    public List<MyReservationResponse> getMyReservations(String token) {
        Long userId = JwtUtil.getUserIdFromToken(token);
        List<MyReservationResponse> reservations = reservationDao.findByMemberId(userId).stream()
                .map(reservation -> new MyReservationResponse(
                        reservation.getId(),
                        reservation.getTheme().getName(),
                        reservation.getDate(),
                        reservation.getTime().getValue(),
                        "예약"
                ))
                .collect(Collectors.toList());

        List<WaitingWithRank> waitings = waitingRepository.findWaitingsWithRankByMemberId(userId);
        List<MyReservationResponse> waitingResponses = waitings.stream()
                .map(waitingWithRank -> new MyReservationResponse(
                        waitingWithRank.getWaiting().getId(),
                        waitingWithRank.getWaiting().getThemeId().toString(),
                        waitingWithRank.getWaiting().getDate(),
                        waitingWithRank.getWaiting().getTimeId().toString(),
                        waitingWithRank.getRank() + "번째 예약대기"
                ))
                .collect(Collectors.toList());

        reservations.addAll(waitingResponses);
        return reservations;
    }
}
