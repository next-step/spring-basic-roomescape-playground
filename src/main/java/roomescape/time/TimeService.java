package roomescape.time;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationDao;

import java.util.List;

@Service
public class TimeService {
    private TimeRepository timeRepository;
    private ReservationDao reservationDao;

    @Autowired
    public TimeService(TimeRepository timeRepository, ReservationDao reservationDao) {
        this.timeRepository = timeRepository;
        this.reservationDao = reservationDao;
    }

    public List<AvailableTime> getAvailableTime(String date, Long themeId) {
        List<Reservation> reservations = reservationDao.findByDateAndThemeId(date, themeId);
        List<Time> times = findAll();

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
        return (List<Time>) timeRepository.findAll();
    }

    public Time save(Time time) {
        return timeRepository.save(time);
    }

    public void deleteById(Long id) {
        timeRepository.deleteById(id);
    }
}
