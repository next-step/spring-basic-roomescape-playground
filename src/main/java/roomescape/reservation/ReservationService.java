package roomescape.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import roomescape.global.auth.Auth;
import roomescape.global.auth.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.reservation.controller.dto.MyReservationResponse;
import roomescape.reservation.controller.dto.ReservationRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(
        ReservationRepository reservationRepository,
        TimeRepository timeRepository,
        ThemeRepository themeRepository,
        MemberRepository memberRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public List<MyReservationResponse> getMyReservations(@Auth LoginMember member) {
        List<Reservation> reservations = reservationRepository.findByMemberId(member.id());
        return reservations.stream()
            .map(MyReservationResponse::from)
            .toList();
    }

    @Transactional
    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        Member member;

        if (reservationRequest.getName() != null) {
            member =  memberRepository.findByName(reservationRequest.getName())
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원이 없습니다"));
        } else {
            member = memberRepository.findById(loginMember.id())
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원이 없습니다"));
        }

        Time time = timeRepository.findById(reservationRequest.getTime())
            .orElseThrow(IllegalArgumentException::new);

        Theme theme = themeRepository.findById(reservationRequest.getTheme())
            .orElseThrow(IllegalArgumentException::new);

        Reservation reservation = new Reservation(
            member.getName(),
            reservationRequest.getDate(),
            member,
            time,
            theme
        );

        Reservation savedReservation = reservationRepository.save(reservation);

        return ReservationResponse.from(savedReservation);
    }

    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
            .map(ReservationResponse::from)
            .toList();
    }
}
