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
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;

    public ReservationService(ReservationRepository reservationRepository, MemberRepository memberRepository,
        ThemeRepository themeRepository, TimeRepository timeRepository) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Member member = memberRepository.getByName(reservationRequest.getName());
        Theme theme = themeRepository.getById(reservationRequest.getTheme());
        Time time = timeRepository.getById(reservationRequest.getTime());

        Reservation reservation = reservationRepository.save(
            new Reservation(reservationRequest.getDate(), member, time, theme)
        );

        return new ReservationResponse(reservation.getId(), reservationRequest.getName(),
            reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getTimeValue());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
            .map(it -> new ReservationResponse(it.getId(), it.getMember().getName(), it.getTheme().getName(),
                it.getDate(),
                it.getTime().getTimeValue()))
            .toList();
    }
}
