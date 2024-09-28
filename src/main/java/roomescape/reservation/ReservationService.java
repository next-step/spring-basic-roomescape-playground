package roomescape.reservation;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;

    public ReservationResponse save(ReservationRequest request) {
        Theme theme = themeRepository.getById(request.getTheme());
        Time time = timeRepository.getById(request.getTime());
        Reservation reservation = Reservation.builder()
            .name(request.getName())
            .date(request.getDate())
            .theme(theme)
            .time(time)
            .build();
        reservation = reservationRepository.save(reservation);
        return new ReservationResponse(
            reservation.getId(),
            request.getName(),
            reservation.getTheme().getName(),
            reservation.getDate(),
            reservation.getTime().getTime()
        );
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
            .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(),
                it.getTime().getTime()))
            .toList();
    }
}
