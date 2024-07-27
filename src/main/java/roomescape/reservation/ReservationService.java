package roomescape.reservation;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.member.LoginMember;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember member) {
        String memberName = reservationRequest.getName();
        String date = reservationRequest.getDate();
        Long themeId = reservationRequest.getTheme();
        Long timeId = reservationRequest.getTime();

        if (memberName == null) {
            memberName = member.name();
        }

        Reservation reservation = reservationDao.save(new ReservationRequest(memberName, date, themeId, timeId));

        return reservationToResponse(reservation);
    }

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll()
                .stream()
                .map(this::reservationToResponse)
                .toList();
    }

    private ReservationResponse reservationToResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                reservation.getTheme().getName(),
                reservation.getTime().getValue()
        );
    }

}
