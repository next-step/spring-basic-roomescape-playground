package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.member.DTO.MemberResponse;
import roomescape.reservation.DTO.ReservationRequest;
import roomescape.reservation.DTO.ReservationResponse;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;

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

    public ReservationResponse save(ReservationRequest reservationRequest, MemberResponse member) {
        String reservatorName = reservationRequest.getName() != null ? reservationRequest.getName() : member.getName();
        Time time = timeRepository.findById(reservationRequest.getTime()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));
        Theme theme = themeRepository.findById(reservationRequest.getTheme()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 ㅌㅔ마입니다,"));

        Reservation newReservation = new Reservation(reservatorName, reservationRequest.getDate(), time, theme);
        Reservation reservation = reservationRepository.save(newReservation);

        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAllWithFetch().stream()
                .map(it -> new ReservationResponse(
                        it.getId(),
                        it.getName(),
                        it.getTheme().getName(),
                        it.getDate(),
                        it.getTime().getValue()
                ))
                .toList();
    }

    public List<ReservationResponse> getMyReservations(String email) {
        return reservationRepository.findByMemberEmail(email).stream()
                .map(it -> new ReservationResponse(it.getId(), it.getTheme().getName(), it.getDate(), it.getTime().getValue(), it.getStatus()))
                .toList();
    }
}
