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

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Member member = memberRepository.getReferenceByName(reservationRequest.name());
        Time time = timeRepository.getReferenceById(reservationRequest.time());
        Theme theme = themeRepository.getReferenceById(reservationRequest.theme());

        Reservation reservation = reservationRepository.save(
                new Reservation(member, reservationRequest.date(), time, theme));

        return new ReservationResponse(reservation.getId(), reservationRequest.name(),
                reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<MyReservationResponse> findMyReservations(Member member) {
        return reservationRepository.findByMemberId(member.getId()).stream()
                .map(it -> new MyReservationResponse(it.getId(), it.getTheme().getName(), it.getDate(),
                        it.getTime().getValue(), "예약"))
                .toList();
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getMember().getName(), it.getTheme().getName(),
                        it.getDate(), it.getTime().getValue()))
                .toList();
    }
}
