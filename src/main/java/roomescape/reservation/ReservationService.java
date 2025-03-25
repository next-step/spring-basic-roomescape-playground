package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;

    public ReservationService(ReservationRepository reservationRepository, ThemeRepository themeRepository, TimeRepository timeRepository) {
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Theme theme = themeRepository.findById(reservationRequest.theme())
                .orElseThrow(() -> new IllegalArgumentException("Invalid theme ID"));
        Time time = timeRepository.findById(reservationRequest.time())
                .orElseThrow(() -> new IllegalArgumentException("Invalid time ID"));

        Reservation reservation = new Reservation(
                reservationRequest.name(),
                reservationRequest.date(),
                time,
                theme
        );

        Reservation savedReservation = reservationRepository.save(reservation);
        return new ReservationResponse(savedReservation.getId(), savedReservation.getName(), savedReservation.getTheme().getName(), savedReservation.getDate(), savedReservation.getTime().getTimeValue());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getTimeValue()))
                .toList();
    }
}
