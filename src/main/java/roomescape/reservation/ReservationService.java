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
    private final MemberRepository memberRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(
        ReservationRepository reservationRepository,
        MemberRepository memberRepository,
        TimeRepository timeRepository,
        ThemeRepository themeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse save(Long memberId, ReservationRequest reservationRequest) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(
                () -> new IllegalArgumentException("not found member with id: " + memberId));

        Time time = timeRepository.findById(reservationRequest.getTime())
            .orElseThrow(() -> new IllegalArgumentException(
                "not found time with id: " + reservationRequest.getTime()));

        Theme theme = themeRepository.findById(reservationRequest.getTheme())
            .orElseThrow(() -> new IllegalArgumentException(
                "not found time with id: " + reservationRequest.getTheme()));
        Reservation reservation = new Reservation(
            reservationRequest.getName(),
            reservationRequest.getDate(),
            time,
            theme,
            member
        );

        reservationRepository.save(reservation);

        return new ReservationResponse(
            reservation.getId(),
            reservationRequest.getName(),
            reservation.getTheme().getName(),
            reservation.getDate(),
            reservation.getTime().getValue()
        );
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
            .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(),
                it.getDate(), it.getTime().getValue()))
            .toList();
    }

    public List<MyReservationResponse> findMyReservations(Long memberId) {
        List<Reservation> reservations =
            reservationRepository.findByMemberIdWithThemeAndTime(memberId);

        return reservations.stream()
            .map(MyReservationResponse::from)
            .toList();
    }
}
