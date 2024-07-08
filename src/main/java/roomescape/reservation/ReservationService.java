package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
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
    private final MemberRepository memberRepository;

    public ReservationService(ReservationRepository reservationRepository,TimeRepository timeRepository,ThemeRepository themeRepository,MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository=timeRepository;
        this.themeRepository=themeRepository;
        this.memberRepository=memberRepository;
    }

    public ReservationResponse save(CreateReservationDto reservationDto) {
        Member member=memberRepository.findById(reservationDto.getMemberId()).get();
        Time time = timeRepository.findById(reservationDto.getTime()).get();
        Theme theme = themeRepository.findById(reservationDto.getTheme()).get();

        Reservation reservation=reservationRepository.save(new Reservation(member, reservationDto.getName(), reservationDto.getDate(), time, theme));

        return new ReservationResponse(reservation.getId(), reservationDto.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getEvent_value());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getEvent_value()))
                .toList();
    }

    public List<MyReservationResponse> findByMemberId(Long memberId){
        List<Reservation> reservations = reservationRepository.findByMemberId(memberId);
        return reservations.stream()
                .map(MyReservationResponse::from)
                .toList();
    }
}
