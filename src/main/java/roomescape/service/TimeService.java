package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.TimeDao;
import roomescape.dto.TimeRequest;
import roomescape.dto.TimeResponse;
import roomescape.model.Reservation;
import roomescape.dao.ReservationDao;

import java.util.List;
import roomescape.dto.AvailableTime;
import roomescape.model.Time;

@Service
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
                        time.getId(),
                        time.getValue(),
                        reservations.stream()
                                .anyMatch(reservation -> reservation.getTime().getId().equals(time.getId()))
                ))
                .toList();
    }

    public List<TimeResponse> findAll() {
        return timeDao.findAll().stream().map((time) -> new TimeResponse(time.getId(), time.getValue())).toList();
    }

    public TimeResponse create(TimeRequest request) {
        Time time = timeDao.save(new Time(request.value()));

        return new TimeResponse(time.getId(), time.getValue());
    }

    public void deleteById(Long id) {
        timeDao.deleteById(id);
    }
}
