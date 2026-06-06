package roomescape.time;

import org.springframework.stereotype.Service;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationRepository;

import java.util.List;

@Service
public class TimeService {

    private final TimeRepository timeRepository;
    private final ReservationRepository reservationRepository;

    public TimeService(TimeRepository timeRepository, ReservationRepository reservationRepository) {
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<AvailableTime> getAvailableTime(String date, Long themeId) {
        List<Reservation> reservations = reservationRepository.findByDateAndThemeId(date, themeId);
        List<Time> times = timeRepository.findAllByDeletedFalse();   // 삭제분 제외

        return times.stream()
                .map(time -> new AvailableTime(
                        time.getId(),
                        time.getTime(),
                        reservations.stream()
                                .anyMatch(reservation -> reservation.getTime().getId().equals(time.getId()))
                ))
                .toList();
    }

    public List<Time> findAll() {
        return timeRepository.findAllByDeletedFalse();   // 삭제분 제외
    }

    public Time save(Time time) {
        return timeRepository.save(time);
    }

    public void deleteById(Long id) {
        Time time = timeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다"));
        time.delete();                  // 물리삭제 → soft delete
        timeRepository.save(time);
    }
}