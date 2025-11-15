package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.auth.LoginMember;
import roomescape.member.Member;
import roomescape.member.Role;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.ParticipationTime;
import roomescape.time.ParticipationTimeRepository;
import roomescape.waiting.Waiting;
import roomescape.waiting.WaitingRepository;
import roomescape.waiting.WaitingWithRank;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {

    private ReservationRepository reservationRepository;
    private ParticipationTimeRepository participationTimeRepository;
    private ThemeRepository themeRepository;
    private WaitingRepository waitingRepository;

    public ReservationService(ReservationRepository reservationRepository, ParticipationTimeRepository participationTimeRepository, ThemeRepository themeRepository, WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.participationTimeRepository = participationTimeRepository;
        this.themeRepository = themeRepository;
        this.waitingRepository = waitingRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        reservationRequest = replaceNameIfEmpty(reservationRequest, loginMember);

        Member member = new Member(loginMember.getId(), loginMember.getName(), loginMember.getEmail(), Role.valueOf(loginMember.getRole()));
        ParticipationTime time = participationTimeRepository.findById(reservationRequest.getTimeId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));
        Theme theme = themeRepository.findById(reservationRequest.getThemeId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));

        Reservation reservation = new Reservation(reservationRequest.getName(), reservationRequest.getDate(), time, theme, member);
        reservation = reservationRepository.save(reservation);
        return new ReservationResponse(reservation.getId(), reservationRequest.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getTime());
    }

    private ReservationRequest replaceNameIfEmpty(ReservationRequest request, LoginMember loginMember) {
        String requestName = request.getName();
        if (requestName == null || requestName.isBlank()) {
            return new ReservationRequest(loginMember.getName(), request.getDate(), request.getThemeId(), request.getTimeId());
        }
        return request;

    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(reservation -> new ReservationResponse(
                        reservation.getId(),
                        reservation.getName(),
                        reservation.getTheme().getName(),
                        reservation.getDate(),
                        reservation.getTime().getTime()
                ))
                .toList();
    }

    public List<MyReservationResponse> findMyReservation(LoginMember loginMembmer) {
        List<Reservation> reservations = reservationRepository.findByMemberId(loginMembmer.getId());
        List<WaitingWithRank> waitings = waitingRepository.findWaitingsWithRankByMemberId(loginMembmer.getId());

        List<MyReservationResponse> myReservationResponses = reservations.stream()
            .map(reservation -> new MyReservationResponse(
                reservation.getId(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getTime(),
                "예약"
            ))
            .collect(Collectors.toList());

List<MyReservationResponse> myReservationResponses = waitings.stream()
    .map(waitingWithRank -> {
        Waiting waiting = waitingWithRank.getWaiting();
        return new MyReservationResponse(
            waiting.getId(),
            waiting.getTheme().getName(),
            waiting.getDate(),
            waiting.getTime().getTime(),
            waitingWithRank.getRank() + "번째 예약대기"
        );
    })
    .collect(Collectors.toList());

        return myReservationResponses;
    }
}
