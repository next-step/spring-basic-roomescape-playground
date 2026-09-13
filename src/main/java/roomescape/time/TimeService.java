package roomescape.time;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import roomescape.exception.DuplicateTimeException;
import roomescape.exception.NotFoundTimeException;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationDao;

import java.util.List;
import java.util.Optional;

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
        Optional<Time> existingTime = timeDao.findByValue(time.getValue());

        if (existingTime.isPresent()) {
            Time foundTime = existingTime.get();
            int restoredCount = timeDao.restoreById(foundTime.getId());

            if (restoredCount > 0) {
                return foundTime;
            }

            throw new DuplicateTimeException("이미 등록된 시간입니다.");
        }
        try {
            return timeDao.save(time);
        } catch (DuplicateKeyException exception) {
            throw new DuplicateTimeException("이미 등록된 시간입니다.");
        }
    }

    public void deleteById(Long id) {
        int deletedCount = timeDao.deleteById(id);

        if (deletedCount == 0) {
            throw new NotFoundTimeException("삭제할 시간을 찾을 수 없습니다.");
        }
    }
}
