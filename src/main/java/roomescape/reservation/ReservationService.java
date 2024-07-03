package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.JwtUtil;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final JwtUtil jwtUtil;

    public ReservationService(ReservationDao reservationDao, JwtUtil jwtUtil) {
        this.reservationDao = reservationDao;
        this.jwtUtil = jwtUtil;
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
                .toList();
    }

    public List<MyReservationResponse> getMyReservations(String token) {
        Long userId = jwtUtil.getUserIdFromToken(token);
        return reservationDao.findByMemberId(userId).stream()
                .map(reservation -> new MyReservationResponse(
                        reservation.getId(),
                        reservation.getTheme().getName(),
                        reservation.getDate(),
                        reservation.getTime().getValue(),
                        "예약"
                ))
                .toList();
    }
}
