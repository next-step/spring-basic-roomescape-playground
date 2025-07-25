package roomescape.reservation;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.auth.LoginMember;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Reservation reservation = reservationDao.save(reservationRequest);

        return new ReservationResponse(reservation.getId(), reservationRequest.getName(),
            reservation.getTheme().getName(), reservation.getDate(),
            reservation.getTime().getValue());
    }

    public ReservationResponse save(ReservationRequest request, LoginMember member) {
        String name = request.getName();

        if (name == null || name.isBlank()) {
            name = member.getName();
            request.setName(name);
        }

        Reservation reservation = reservationDao.save(request);

        return new ReservationResponse(
            reservation.getId(),
            name,
            reservation.getTheme().getName(),
            reservation.getDate(),
            reservation.getTime().getValue()
        );
    }

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
            .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(),
                it.getDate(), it.getTime().getValue()))
            .toList();
    }
}
