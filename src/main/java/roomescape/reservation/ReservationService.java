package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.member.LoginMember;
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
    private final MemberRepository memberRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;

    public ReservationService(ReservationRepository reservationRepository, MemberRepository memberRepository, ThemeRepository themeRepository, TimeRepository timeRepository) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        reservationRequest = reservationRequest.withUserName(loginMember.getName());

        Member member = getMemberBy(loginMember.getId());

        Time time = timeRepository.findById(reservationRequest.getTime())
                .orElseThrow(() -> new IllegalArgumentException("Invalid time ID"));
        Theme theme = themeRepository.findById(reservationRequest.getTheme())
                .orElseThrow(() -> new IllegalArgumentException("Invalid theme ID"));

        Reservation reservation = Reservation.of(
                reservationRequest.getName(),
                reservationRequest.getDate(),
                member,
                time,
                theme
        );
        reservationRepository.save(reservation);

        return new ReservationResponse(
                reservation.getId(), 
                reservationRequest.getName(), 
                theme.getName(),
                reservation.getDate(), 
                time.getTime()
        );
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getTime()))
                .toList();
    }

    public List<MyReservationResponse> getMyReservationResponses(Long memberId) {
        Member member = getMemberBy(memberId);

        List<Reservation> reservations = reservationRepository.findAllByMember(member);
        return reservations.stream()
                .map(r -> new MyReservationResponse(
                        r.getId(),
                        r.getTheme().getName(),
                        r.getDate(),
                        r.getTime().getTime(),
                        "예약"
                ))
                .toList();
    }

    private Member getMemberBy(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));
    }
}
