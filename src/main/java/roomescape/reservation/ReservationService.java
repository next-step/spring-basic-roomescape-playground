package roomescape.reservation;

import org.springframework.stereotype.Service;

import java.util.List;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
public class ReservationService {

    private ReservationRepository reservationRepository;
    private MemberRepository memberRepository;
    private TimeRepository timeRepository;
    private ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository, MemberRepository memberRepository,TimeRepository timeRepository,ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.timeRepository=timeRepository;
        this.themeRepository=themeRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest,
        LoginMember loginMember) {
        Member member;
        if (reservationRequest.getName() != null) {
            member = memberRepository.findByName(reservationRequest.getName());
        } else {
            member = memberRepository.findById(loginMember.getId()).orElseThrow();
        }
        Time time= timeRepository.findById(reservationRequest.getTime()).orElseThrow();
        Theme theme=themeRepository.findById(reservationRequest.getTheme()).orElseThrow();
        Reservation unSavedReservation=new Reservation(member.getName(), reservationRequest.getDate(),time,theme);
        Reservation reservation = reservationRepository.save(unSavedReservation);

        return new ReservationResponse(reservation.getId(), reservation.getName(),
            reservation.getTheme().getName(), reservation.getDate(),
            reservation.getTime().getValue());
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
}
