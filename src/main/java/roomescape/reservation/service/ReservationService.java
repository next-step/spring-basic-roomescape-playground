package roomescape.reservation.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.reservation.dto.MyReservationResponse;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.model.Waiting;
import roomescape.waiting.repository.WaitingRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final WaitingRepository waitingRepository;
    private final MemberRepository memberRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;

    public ReservationService(ReservationRepository reservationRepository, WaitingRepository waitingRepository,
        MemberRepository memberRepository, ThemeRepository themeRepository, TimeRepository timeRepository) {
        this.reservationRepository = reservationRepository;
        this.waitingRepository = waitingRepository;
        this.memberRepository = memberRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
    }

    @Transactional
    public ReservationResponse save(ReservationRequest reservationRequest) {
        Member member = memberRepository.getByName(reservationRequest.getName());
        Theme theme = themeRepository.getById(reservationRequest.getTheme());
        Time time = timeRepository.getById(reservationRequest.getTime());

        Reservation reservation = reservationRepository.save(
            new Reservation(reservationRequest.getDate(), member, time, theme)
        );

        return new ReservationResponse(reservation.getId(), reservationRequest.getName(),
            reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getTimeValue());
    }

    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
            .map(it -> new ReservationResponse(it.getId(), it.getMember().getName(), it.getTheme().getName(),
                it.getDate(),
                it.getTime().getTimeValue()))
            .toList();
    }

    public List<MyReservationResponse> getMyReservations(LoginMember loginMember) {
        Member member = memberRepository.getById(loginMember.getId());

        List<MyReservationResponse> reservationResponses = new ArrayList<>(
            reservationRepository.findByMember(member).stream()
                .map(r -> new MyReservationResponse(
                    r.getId(), r.getTheme().getName(), r.getDate(), r.getTime().getTimeValue(), "예약"
                )).toList()
        );

        List<MyReservationResponse> waitingResponses = waitingRepository.findWaitingWithRankByMemberId(
                loginMember.getId())
            .stream()
            .map(w -> {
                Waiting waiting = w.waiting();
                return new MyReservationResponse(
                    waiting.getId(),
                    waiting.getTheme().getName(),
                    waiting.getDate(),
                    waiting.getTime().getTimeValue(),
                    (w.rank() + 1) + "번째 예약대기"
                );
            })
            .toList();

        reservationResponses.addAll(waitingResponses);
        reservationResponses.sort(
            Comparator.comparing(MyReservationResponse::date)
                .thenComparing(MyReservationResponse::time)
        );

        return reservationResponses;
    }
}
