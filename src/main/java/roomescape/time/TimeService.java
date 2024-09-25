package roomescape.time;

import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationRepository;

@Service
public class TimeService {

    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;

    public TimeService(ReservationRepository reservationRepository, TimeRepository timeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
    }

    public List<AvailableTime> getAvailableTime(String date, Long themeId) {
        List<Reservation> reservations = reservationRepository.findByDateAndThemeId(date, themeId);
        List<Time> times = timeRepository.findAll();

        return times.stream()
            .map(time -> new AvailableTime(
                time.getId(),
                time.getTimeValue(),
                reservations.stream()
                    .anyMatch(reservation -> reservation.getTime().getId().equals(time.getId()))
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
