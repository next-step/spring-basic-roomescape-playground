package roomescape.reservation;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.web.server.ResponseStatusException;
import roomescape.auth.domain.LoginMember;
import roomescape.error.ErrorMessage;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository, TimeRepository timeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        if (loginMember.notHaveName(reservationRequest.getName()) && loginMember.isNotAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ErrorMessage.FORBIDDEN_RESERVATION.getMessage());
        }

        Time time = timeRepository.findById(reservationRequest.getTime())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.TIME_NOT_FOUND.getMessage()));
        Theme theme = themeRepository.findById(reservationRequest.getTheme())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.THEME_NOT_FOUND.getMessage()));

        Reservation reservation = new Reservation(reservationRequest.getName(), reservationRequest.getDate(), time, theme);
        Reservation savedReservation = reservationRepository.save(reservation);

        return new ReservationResponse(savedReservation.getId(), savedReservation.getName(),
                savedReservation.getTheme().getName(), savedReservation.getDate(), savedReservation.getTime().getValue());
    }

    public void deleteById(Long id, LoginMember loginMember) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.RESERVATION_NOT_FOUND.getMessage()));

        if (loginMember.isNotAdmin() && loginMember.notHaveName(reservation.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ErrorMessage.FORBIDDEN_DELETE.getMessage());
        }

        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }
}
