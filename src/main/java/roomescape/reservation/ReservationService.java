package roomescape.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {
    final private ReservationRepository reservationRepository;
    final private TimeRepository timeRepository;
    final private ThemeRepository themeRepository;
    final private MemberRepository memberRepository;

    public ReservationResponse save(CreateReservationDto reservationDto) {
        Member member = memberRepository.findById(reservationDto.getMemberId()).get();
        Time time = timeRepository.findById(reservationDto.getTime()).get();
        Theme theme = themeRepository.findById(reservationDto.getTheme()).get();

        Reservation reservation = reservationRepository.save(new Reservation(member, reservationDto.getName(), reservationDto.getDate(), time, theme));

        return new ReservationResponse(reservation.getId(), reservationDto.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getTime_value());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getTime_value()))
                .toList();
    }

    public List<MyReservationResponse> findByMemberId(Long id) {
        List<Reservation> reservations = reservationRepository.findByMemberId(id);
        return reservations.stream()
                .map(MyReservationResponse::from)
                .toList();
    }
}
