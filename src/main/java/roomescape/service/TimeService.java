package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.TimeDao;
import roomescape.model.Reservation;
import roomescape.dao.ReservationDao;

import java.util.List;
import roomescape.dto.AvailableTime;
import roomescape.model.Time;

@Service
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

    public Time save(Time time) {
        return timeDao.save(time);
    }

    public void deleteById(Long id) {
        timeDao.deleteById(id);
    }
}
