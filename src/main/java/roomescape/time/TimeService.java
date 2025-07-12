package roomescape.time;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationRepository;

@Service
@Transactional(readOnly = true)
public class TimeService {

    private final TimeRepository timeRepo;
    private final ReservationRepository reservationRepo;

    public TimeService(
            TimeRepository timeRepo,
            ReservationRepository reservationRepo
    ) {
        this.timeRepo = timeRepo;
        this.reservationRepo = reservationRepo;
    }

    public List<AvailableTime> getAvailableTime(String date, Long themeId) {
        List<Reservation> reservations =
                reservationRepo.findByDateAndThemeId(date, themeId);
        List<Time> times = timeRepo.findByDeletedFalse();

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
        return timeRepo.findByDeletedFalse();
    }

    @Transactional
    public Time save(Time time) {
        return timeRepo.save(time);
    }

    @Transactional
    public void deleteById(Long id) {
        timeRepo.deleteById(id);
    }
}
