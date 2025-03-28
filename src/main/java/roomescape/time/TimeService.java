package roomescape.time;

import org.springframework.stereotype.Service;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class TimeService {
    private final TimeRepository timeRepository;
    private final ReservationRepository reservationRepository;

    public TimeService(TimeRepository timeRepository, ReservationRepository reservationRepository) {
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<AvailableTime> getAvailableTime(LocalDate date, Long themeId) {
        List<Reservation> reservations = reservationRepository.findByDateAndThemeId(date, themeId);
        List<Time> times = timeRepository.findAll();

        return times.stream()
                .map(time -> new AvailableTime(
                        time.getId(),
                        time.getValue(),
                        isTimeReserved(time, reservations)
                ))
                .toList();
    }

    public List<Time> findAll() {
        return timeRepository.findAll();
    }

    public TimeResponse save(TimeRequest request) {
        Time time = new Time(null, request.value());
        Time savedTime = timeRepository.save(time);
        return new TimeResponse(savedTime.getId(), savedTime.getValue());
    }

    public void deleteById(Long id) {
        timeRepository.deleteById(id);
    }

    private boolean isTimeReserved(Time time, List<Reservation> reservations) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.getTime().getId().equals(time.getId()));
    }
}
