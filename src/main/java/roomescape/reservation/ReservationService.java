package roomescape.reservation;

import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import roomescape.global.auth.JwtService;
import roomescape.reservation.dto.MyReservationResponse;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReservationService {

    private ReservationRepository reservationRepository;
    private TimeRepository timeRepository;
    private ThemeRepository themeRepository;

    private JwtService jwtService;

    public ReservationService(ReservationRepository reservationRepository, TimeRepository timeRepository, ThemeRepository themeRepository, JwtService jwtService) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.jwtService = jwtService;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {

        Optional<Time> timeOptional = timeRepository.findById(reservationRequest.getTime());
        Optional<Theme> themeOptional = themeRepository.findById(reservationRequest.getTheme());

        if (timeOptional.isEmpty() || themeOptional.isEmpty()) return null;

        Reservation reservation= reservationRepository.save(new Reservation(reservationRequest.getName(), reservationRequest.getDate(),timeOptional.get(), themeOptional.get()));
        return new ReservationResponse(reservation.getId(), reservationRequest.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    public List<MyReservationResponse> findMyReservations(Cookie[] cookies) {
        Long memberId = jwtService.decodeToken(jwtService.extractTokenFromCookie(cookies));
        List<Reservation> reservations = reservationRepository.findByMemberId(memberId);

        for (Reservation r : reservations) {
            log.info("{}", r.getDate());
        }

        return reservations.stream()
                .map(reservation -> new MyReservationResponse(reservation.getId(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue(), "예약"))
                .toList();



    }
}
