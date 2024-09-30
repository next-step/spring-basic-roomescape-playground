package roomescape.reservation;

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
import roomescape.reservation.controller.dto.WaitingRequest;
import roomescape.reservation.controller.dto.WaitingResponse;
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
        List<WaitingWithRank> waitingWithRanks = reservationRepository.findWaitingsWithRankByMemberId(member.id());
        return waitingWithRanks.stream()
            .map(MyReservationResponse::from)
            .toList();
    }

    @Transactional
    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        Member member;

        if (reservationRequest.getName() != null) {
            member =  memberRepository.findByName(reservationRequest.getName())
                .orElseThrow(IllegalArgumentException::new);
        } else {
            member = memberRepository.getById(loginMember.id());
        }

        Time time = timeRepository.getById(reservationRequest.getTime());

        Theme theme = themeRepository.getById(reservationRequest.getTheme());

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
    public WaitingResponse createWaiting(LoginMember loginMember, WaitingRequest request) {
        Member member = memberRepository.getById(loginMember.id());

        Time time = timeRepository.getById(request.time());

        Theme theme = themeRepository.getById(request.theme());

        boolean exists = reservationRepository.existsByDateAndTimeAndThemeAndMember(
            request.date(), time, theme, member
        );

        if (exists) {
            throw new IllegalArgumentException("이미 해당 테마, 시간, 날짜에 예약이 존재합니다.");
        }

        Reservation reservation = new Reservation(
            member.getName(),
            request.date(),
            member,
            time,
            theme
        );

        Reservation savedReservation = reservationRepository.save(reservation);

        Long rank = reservationRepository.countByDateAndThemeIdAndTimeIdAndIdLessThan(
            savedReservation.getDate(),
            savedReservation.getTheme().getId(),
            savedReservation.getTime().getId(),
            savedReservation.getId()
        );

        return WaitingResponse.of(savedReservation, rank);
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
