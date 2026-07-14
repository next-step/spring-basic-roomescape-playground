package roomescape.time;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.ErrorCode;
import roomescape.exception.NotFoundException;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationDao;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TimeService {
    private final TimeDao timeDao;
    private final ReservationDao reservationDao;

    public TimeService(TimeDao timeDao, ReservationDao reservationDao) {
        this.timeDao = timeDao;
        this.reservationDao = reservationDao;
    }

    public List<AvailableTime> getAvailableTime(String date, Long themeId) {
        List<Reservation> reservations = reservationDao.findByDateAndThemeId(date, themeId);
        List<Time> times = timeDao.findAll();

        return times.stream()
                .map(time -> new AvailableTime(
                        time.id(),
                        time.value(),
                        reservations.stream()
                                .anyMatch(reservation -> reservation.time().id().equals(time.id()))
                ))
                .toList();
    }

    public List<Time> findAll() {
        return timeDao.findAll();
    }

    @Transactional
    public Time save(Time time) {
        return timeDao.save(time);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!timeDao.deleteById(id)) {
            throw new NotFoundException(ErrorCode.TIME_NOT_FOUND);
        }
    }
}
