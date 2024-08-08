package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import roomescape.reservation.controller.dto.ReservationRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;
import roomescape.time.domain.Time;
import roomescape.time.domain.TimeRepository;

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

    public ReservationResponse save(String memberName,
                                    ReservationRequest reservationRequest) {
        Reservation reservation = reservationRepository.save(requestToReservation(memberName, reservationRequest));
        return reservationToResponse(reservation);
    }

    private ReservationResponse reservationToResponse(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    private Reservation requestToReservation(String memberName, ReservationRequest reservationRequest) {
        Theme themeProxy = findThemeProxyById(reservationRequest.theme());
        Time timeProxy = findTimeProxyById(reservationRequest.time());
        return new Reservation(memberName, reservationRequest.date(), timeProxy, themeProxy);
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll()
                .stream()
                .map(this::reservationToResponse)
                .toList();
    }

    private Theme findThemeProxyById(Long id) {
        return themeRepository.getReferenceById(id);
    }

    private Time findTimeProxyById(Long id) {
        return timeRepository.getReferenceById(id);
    }
}
