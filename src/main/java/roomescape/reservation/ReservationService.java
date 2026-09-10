package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.member.LoginMember;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public ReservationResponse save(ReservationRequest request, LoginMember loginMember) {
        String name = resolveName(request, loginMember);
        Reservation reservation = reservationDao.save(request, name);
        return new ReservationResponse(reservation.getId(), name,
                reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    private String resolveName(ReservationRequest request, LoginMember loginMember) {
        if (request.getName() != null && !request.getName().isBlank()) {
            return request.getName();
        }
        return loginMember.getName();
    }

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }
}
