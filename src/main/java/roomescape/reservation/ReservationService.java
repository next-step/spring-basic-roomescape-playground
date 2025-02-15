package roomescape.reservation;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(ReservationRepository reservationRepository, TimeRepository timeRepository,
                              ThemeRepository themeRepository, MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, String email) {
        Member member = memberRepository.findByEmailOrThrow(email);
        Time time = timeRepository.findByIdOrThrow(reservationRequest.getTime());
        Theme theme = themeRepository.findByIdOrThrow(reservationRequest.getTheme());
        Reservation reservation = new Reservation(reservationRequest.getName(), reservationRequest.getDate(), time,
                theme, member);
        Reservation savedReservation = reservationRepository.save(reservation);

        return new ReservationResponse(savedReservation.getId(), savedReservation.getName(),
                savedReservation.getTheme().getName(), savedReservation.getDate(),
                savedReservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(),
                        it.getTime().getValue()))
                .toList();
    }
}
