package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRepository;
import roomescape.reservation.controller.dto.MyReservationResponse;
import roomescape.reservation.controller.dto.ReservationRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;
import roomescape.time.domain.Time;
import roomescape.time.domain.TimeRepository;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(ReservationRepository reservationRepository, ThemeRepository themeRepository, TimeRepository timeRepository, MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.memberRepository = memberRepository;
    }

    public List<MyReservationResponse> findMyReservations(String memberName) {
        List<Reservation> myReservations = reservationRepository.findMyReservationsByName(memberName);
        return myReservations.stream()
                .map(r -> new MyReservationResponse(
                        r.getId(),
                        r.getTheme().getName(),
                        r.getDate(),
                        r.getTime().getValue(),
                        "예약"
                ))
                .toList();
    }

    public ReservationResponse save(String memberName,
                                    ReservationRequest reservationRequest) {
        Reservation reservation = reservationRepository.save(requestToReservation(memberName, reservationRequest));
        return reservationToResponse(reservation);
    }

    private ReservationResponse reservationToResponse(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    private Reservation requestToReservation(String memberName, ReservationRequest reservationRequest) {
        Member member = findMemberByName(memberName);
        Theme themeProxy = findThemeProxyById(reservationRequest.theme());
        Time timeProxy = findTimeProxyById(reservationRequest.time());
        return new Reservation(memberName, reservationRequest.date(), member, timeProxy, themeProxy);
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll()
                .stream()
                .map(this::reservationToResponse)
                .toList();
    }

    private Member findMemberByName(String name) {
        return memberRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 멤버입니다."));
    }

    private Theme findThemeProxyById(Long id) {
        return themeRepository.getReferenceById(id);
    }

    private Time findTimeProxyById(Long id) {
        return timeRepository.getReferenceById(id);
    }
}
