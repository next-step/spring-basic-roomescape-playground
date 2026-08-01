package roomescape.time;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.ReservationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TimeService {

    private final TimeRepository timeRepository;
    private final ReservationRepository reservationRepository;

    public TimeService(TimeRepository timeRepository, ReservationRepository reservationRepository) {
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<Time> findAllActiveTimes() {
        return timeRepository.findByDeletedFalse();
    }

    @Transactional
    public Time createTime(Time time) {
        if (time.getValue() == null || time.getValue().isEmpty()) {
            throw new IllegalArgumentException("시간 값이 비어있을 수 없습니다.");
        }
        return timeRepository.save(time);
    }

    @Transactional
    public void deleteTime(Long id) {
        timeRepository.deleteById(id);
    }

    public List<AvailableTime> getAvailableTimes(String date, Long themeId) {
        List<Time> times = timeRepository.findByDeletedFalse();
        List<AvailableTime> result = new ArrayList<>();

        for (Time t : times) {
            boolean isBooked = reservationRepository.existsByDateAndTimeAndTheme(date, t.getId(), themeId);
            result.add(new AvailableTime(t.getId(), t.getValue(), isBooked));
        }
        return result;
    }
}
