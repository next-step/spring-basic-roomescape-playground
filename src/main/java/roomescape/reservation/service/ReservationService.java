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
import roomescape.waiting.domain.Waiting;
import roomescape.waiting.domain.WaitingRepository;

import java.util.List;

import static java.util.stream.Stream.*;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final WaitingRepository waitingRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              WaitingRepository waitingRepository,
                              ThemeRepository themeRepository,
                              TimeRepository timeRepository,
                              MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.waitingRepository = waitingRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.memberRepository = memberRepository;
    }

    public List<MyReservationResponse> findMyReservations(String memberName) {
        List<Reservation> myReservations = reservationRepository.findMyReservationsByName(memberName);
        List<Waiting> myWaitings = waitingRepository.findMyWaitingByName(memberName);
        List<MyReservationResponse> reservationResponses = myReservations.stream()
                .map(this::myReservationToResponse)
                .toList();
        List<MyReservationResponse> waitingResponses = myWaitings.stream()
                .map(this::myWaitingToResponse)
                .toList();
        return concat(reservationResponses.stream(), waitingResponses.stream()).toList();
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

    private MyReservationResponse myReservationToResponse(Reservation reservation) {
        return new MyReservationResponse(
                reservation.getId(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue(),
                "예약"
        );
    }

    private MyReservationResponse myWaitingToResponse(Waiting waiting) {
        return new MyReservationResponse(
                waiting.getId(),
                waiting.getTheme().getName(),
                waiting.getDate(),
                waiting.getTime().getValue(),
                (waitingRepository.countWaitingBy(
                        waiting.getTheme(),
                        waiting.getTime(),
                        waiting.getDate(),
                        waiting.getId()) + 1) + "번째 예약대기"
        );
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
