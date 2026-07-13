package roomescape.time.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationDao;
import roomescape.time.domain.Time;
import roomescape.time.dto.AvailableTime;
import roomescape.time.repository.TimeDao;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TimeService {
    private TimeDao timeDao;
    private ReservationDao reservationDao;

    public TimeService(TimeDao timeDao, ReservationDao reservationDao) {
        this.timeDao = timeDao;
        this.reservationDao = reservationDao;
    }

    public List<AvailableTime> getAvailableTime(String date, Long themeId) {
        List<Reservation> reservations = reservationDao.findByDateAndThemeId(date, themeId);
        List<Time> times = timeDao.findAll();

        return times.stream()
                .map(time -> new AvailableTime(
                        time.getId(),
                        time.getValue(),
                        reservations.stream()
                                .anyMatch(reservation -> reservation.getTime().getId().equals(time.getId()))
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
        timeDao.deleteById(id);
    }
}
