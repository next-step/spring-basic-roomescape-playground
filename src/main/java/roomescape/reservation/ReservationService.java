package roomescape.reservation;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {
    private ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Reservation reservation = reservationDao.save(reservationRequest);

        // ReservationResponse를 생성할 때 Reservation의 theme 이름과 time 값을 사용
        return new ReservationResponse(
                reservation.getId(),
                reservationRequest.getName(),
                reservation.getTheme().getName(), // Theme의 이름을 가져옴
                reservation.getDate(),
                reservation.getTime().getValue() // Time의 값(value)을 가져옴
        );
    }

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    public List<ReservationResponse> findAllByMember(Long memberId) {
        List<Reservation> reservations = reservationDao.findByMemberId(memberId);
        return reservations.stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }
}
