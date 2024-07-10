package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.member.model.Member;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;

@Service
public class ReservationService {
    private ReservationRepository reservationRepository;
    private TimeRepository timeRepository;
    private ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository, TimeRepository timeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Time time = timeRepository.findById(reservationRequest.getTime()).orElseThrow();
        Theme theme = themeRepository.findById(reservationRequest.getTheme()).orElseThrow();
        Reservation reservation = reservationRepository.save(new Reservation(reservationRequest.getName(), reservationRequest.getDate(), time, theme));
        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }


    public List<MyReservationResponse> findMyReservations(Long memberId) {

        return reservationRepository.findByMemberId(memberId).stream()
                .map(it-> MyReservationResponse.builder()
                        .reservationId(it.getReservationId())
                        .date(it.getDate())
                        .status(it.getStatus())
                        .time(it.getTime())
                        .theme(it.getTheme())
                        .build())
                .toList();
    }
}
