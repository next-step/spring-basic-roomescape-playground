package roomescape.time;

import org.springframework.stereotype.Service;
import roomescape.reservation.ReservationDao;
import roomescape.reservation.domain.Reservation;
import roomescape.time.domain.AvailableTime;
import roomescape.time.domain.Time;

import java.util.List;
import roomescape.time.repository.TimeRepository;

@Service
public class TimeService {

    private TimeRepository timeRepository;
    private ReservationDao reservationDao;

    public TimeService(TimeRepository timeRepository, ReservationDao reservationDao) {
        this.timeRepository = timeRepository;
        this.reservationDao = reservationDao;
    }

    public List<AvailableTime> getAvailableTime(String date, Long themeId) {
        List<Reservation> reservations = reservationDao.findByDateAndThemeId(date, themeId);
        List<Time> times = timeRepository.findAll();

        return times.stream()
                .map(time -> new AvailableTime(
                        time.getId(),
                        time.getValue(),
                        reservations.stream()
                                .anyMatch(reservation -> reservation.getTime().getId()
                                        .equals(time.getId()))
                ))
                .toList();
    }

    public List<Time> findAll() {
        return timeRepository.findAll();
    }

    public Time save(Time time) {
        return timeRepository.save(time);
    }

    public void deleteById(Long id) {
        timeRepository.deleteById(id);
    }
}
