package roomescape.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    final private ReservationRepository reservationRepository;
    final private TimeRepository timeRepository;
    final private ThemeRepository themeRepository;

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Time time = timeRepository.findById(reservationRequest.getTime()).get();
        Theme theme = themeRepository.findById(reservationRequest.getTheme()).get();

        Reservation reservation = reservationRepository.save(new Reservation(null, reservationRequest.getName(), reservationRequest.getDate(), time, theme));

        return new ReservationResponse(reservation.getId(), reservationRequest.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getTime_value());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getTime_value()))
                .toList();
    }
}
