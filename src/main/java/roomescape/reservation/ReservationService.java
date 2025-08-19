package roomescape.reservation;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.dto.LoginMember;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;

    public ReservationService(ReservationRepository reservationRepository,
        ThemeRepository themeRepository, TimeRepository timeRepository) {
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
    }

    @Transactional
    public ReservationResponse save(ReservationRequest request) {
        Theme theme = themeRepository.findById(request.getTheme())
            .orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 테마입니다. id=" + request.getTheme()));
        Time time = timeRepository.findById(request.getTime())
            .orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 시간입니다. id=" + request.getTime()));

        Reservation reservation = new Reservation(
            request.getName(),
            request.getDate(),
            time,
            theme
        );

        Reservation saved = reservationRepository.save(reservation);

        return new ReservationResponse(saved.getId(), saved.getName(),
            saved.getTheme().getName(), saved.getDate(),
            saved.getTime().getTime());
    }

    @Transactional
    public ReservationResponse save(ReservationRequest request, LoginMember member) {
        String name = request.getName();

        if (name == null || name.isBlank()) {
            name = member.name();
            request.setName(name);
        }
        return save(request);
    }

    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
            .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(),
                it.getDate(), it.getTime().getTime()))
            .toList();
    }
}
