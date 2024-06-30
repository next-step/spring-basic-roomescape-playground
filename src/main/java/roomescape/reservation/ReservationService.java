package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.instructure.JwtTokenProvider;
import roomescape.member.*;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    private ReservationRepository reservationRepository;
    private ThemeRepository themeRepository;
    private TimeRepository timeRepository;
    private MemberRepository memberRepository;
    private MemberService memberService;

    public ReservationService(ReservationRepository reservationRepository, ThemeRepository themeRepository, TimeRepository timeRepository, MemberRepository memberRepository, MemberService memberService) {
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.memberRepository = memberRepository;
        this.memberService = memberService;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Time time = timeRepository.findById(reservationRequest.getTime())
                .orElseThrow(() -> new IllegalArgumentException("Time not found"));

        Theme theme = themeRepository.findById(reservationRequest.getTheme())
                .orElseThrow(() -> new IllegalArgumentException("Theme not found"));

        Member member = null;
        if (reservationRequest.getMember() != null) {
            member = memberRepository.findById(reservationRequest.getMember())
                    .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        }

        Reservation reservation = new Reservation(
                reservationRequest.getName(),
                reservationRequest.getDate(),
                time,
                theme,
                member
        );

        reservation = reservationRepository.save(reservation);

        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
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
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    public List<MyReservationResponse> findReservation(LoginMember loginMember) {
        List<MyReservationResponse> reservations = reservationRepository.findByMemberId(loginMember.getId()).stream()
                .map(it -> new MyReservationResponse(it.getId(),
                        it.getTheme().getName(),
                        it.getDate(),
                        it.getTime().getValue(), "예약"))
                .collect(Collectors.toList());
        return reservations;
    }
}