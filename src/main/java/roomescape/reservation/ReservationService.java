package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    private ReservationRepository reservationRepository;
    private TimeRepository timeRepository;
    private ThemeRepository themeRepository;
    private MemberRepository memberRepository;

    public ReservationService(ReservationRepository reservationRepository, TimeRepository timeRepository, ThemeRepository themeRepository, MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {

        Optional<Time> timeOptional = timeRepository.findById(reservationRequest.getTime());
        Optional<Theme> themeOptional = themeRepository.findById(reservationRequest.getTheme());
        Optional<Member> memberOptional = memberRepository.findById(reservationRequest.getMember());

        if(timeOptional.isEmpty() || themeOptional.isEmpty()) {
            return null;
        }

        Reservation reservation = reservationRepository.save(new Reservation(
                reservationRequest.getName(),
                reservationRequest.getDate(),
                memberOptional.orElseThrow(),
                timeOptional.get(),
                themeOptional.get()
        ));

        return new ReservationResponse(reservation.getId(), reservationRequest.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    public List<MyReservationResponse> findAllByMember(LoginMember loginMember) {
        return reservationRepository.findByMemberId(loginMember.getId())
                .stream()
                .map(reservation -> new MyReservationResponse(
                        reservation.getId(),
                        reservation.getTheme().getName(),
                        reservation.getTime().getValue(),
                        reservation.getDate(),
                        "예약"))
                .toList();
    }
}
