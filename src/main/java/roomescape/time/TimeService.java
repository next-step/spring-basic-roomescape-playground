package roomescape.time;

import org.springframework.stereotype.Service;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationRepository;

import java.util.List;
import java.util.NoSuchElementException;

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
        List<Time> times = timeRepository.findAllByDeletedFalse();

        return times.stream()
                    .map(time -> new AvailableTime(
                            time.getId(),
                            time.getValue(),
                            reservations.stream()
                                        .anyMatch(reservation ->
                                                reservation.getTime().getId().equals(time.getId()))))
                    .toList();
    }

    public List<TimeResponse> findAll() {
        return timeRepository.findAllByDeletedFalse()
                             .stream()
                             .map(time ->
                                     new TimeResponse(
                                             time.getId(),
                                             time.getValue()
                                     )
                             )
                             .toList();
    }

    public TimeResponse save(TimeRequest request) {
        Time time = new Time(request.getValue());
        Time savedTime = timeRepository.save(time);

        return new TimeResponse(savedTime.getId(), savedTime.getValue());
    }

    public void deleteById(Long id) {
        Time time = timeRepository.findById(id).orElseThrow(NoSuchElementException::new);
        time.delete();
        timeRepository.save(time);
    }
}
