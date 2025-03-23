package roomescape.reservation;

import org.springframework.stereotype.Service;
import java.util.List;
import roomescape.global.exception.RoomescapeNotFoundException;
import roomescape.reservationTime.ReservationTimeRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeDao;
import roomescape.reservationTime.ReservationTime;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ThemeDao themeDao;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(ReservationDao reservationDao, ThemeDao themeDao,
                              ReservationTimeRepository reservationTimeRepository) {
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Theme theme = themeDao.findById(reservationRequest.theme());
        ReservationTime reservationTime = reservationTimeRepository.findById(reservationRequest.time())
                .orElseThrow(() -> new RoomescapeNotFoundException("예약 시간을 찾을 수 없습니다."));
        Reservation reservation = reservationDao.save(reservationRequest.toReservation(theme, reservationTime));

        return new ReservationResponse(reservation);
    }

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(it -> new ReservationResponse(it))
                .toList();
    }
}
