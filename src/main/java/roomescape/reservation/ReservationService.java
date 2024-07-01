package roomescape.reservation;

import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private TimeRepository timeRepository;
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private MemberRepository memberRepository;

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        Time time = timeRepository.findById(reservationRequest.getTime()).orElseThrow();
        Theme theme = themeRepository.findById(reservationRequest.getTheme()).orElseThrow();
        Member member = memberRepository.findById(loginMember.getId()).orElseThrow();

        reservationRequest.setName(loginMember.getName());
        reservationRequest.setMemberId(loginMember.getId());

        Reservation reservation =
                reservationRepository.save(new Reservation(reservationRequest.getName(), reservationRequest.getDate(), time, theme, member));

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

    public List<MyReservationResponse> findMyList(LoginMember loginMember) {
        return reservationRepository.findByMemberId(loginMember.getId()).stream()
                .map(it -> new MyReservationResponse(it.getId(), it.getTheme().getName(), it.getDate(), it.getTime().getValue(), it.getName()))
                .toList();
    }
}
