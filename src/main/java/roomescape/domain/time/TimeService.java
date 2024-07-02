package roomescape.domain.time;

import org.springframework.stereotype.Service;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDao;

import java.util.List;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.repository.TimeRepository;

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
                                .anyMatch(reservation -> reservation.getTime().getId().equals(time.getId()))
                ))
                .toList();
    }

    /**
     * 컨트롤러 계층에 DTO를 반환하도록 리팩토링
     * https://tecoble.techcourse.co.kr/post/2021-04-25-dto-layer-scope/
     */
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
