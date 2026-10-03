package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.auth.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.Waiting;
import roomescape.waiting.WaitingService;
import roomescape.waiting.WaitingWithRank;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final WaitingService waitingService;
    private final MemberRepository memberRepository;


    public ReservationService(ReservationRepository reservationRepository,
                              TimeRepository timeRepository,
                              ThemeRepository themeRepository, WaitingService waitingService, MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.waitingService = waitingService;
        this.memberRepository = memberRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember member) {
        ReservationRequest reservationToSave = withName(reservationRequest, member);
        Time time = timeRepository.findById(reservationRequest.getTime())
                .orElseThrow(() -> new IllegalArgumentException("시간이 존재하지 않습니다."));
        Theme theme = themeRepository.findById(reservationRequest.getTheme())
                .orElseThrow(() -> new IllegalArgumentException("테마가 존재하지 않습니다."));
        Member persistMember = memberRepository.findById(member.id())
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        Reservation reservation = new Reservation(
                reservationToSave.getName(),
                reservationToSave.getDate(),
                time,
                theme,
                persistMember
        );
        Reservation savedReservation = reservationRepository.save(reservation);
        return new ReservationResponse(savedReservation.getId(),
                savedReservation.getName(),
                savedReservation.getTheme().getName(),
                savedReservation.getDate(),
                savedReservation.getTime().getValue());
    }



    public List<MyReservationResponse> findMyReservations(LoginMember member) {
        List<Reservation> reservations = reservationRepository.findByMemberId(member.id());
        List<MyReservationResponse> responses = new ArrayList<>();

        for (Reservation reservation : reservations) {
            MyReservationResponse response = new MyReservationResponse(
                    reservation.getId(),
                    reservation.getTheme().getName(),
                    reservation.getDate(),
                    reservation.getTime().getValue(),
                    "예약"
            );
            responses.add(response);
        }
        List<WaitingWithRank> waitingWithRanks = waitingService.findWaitingsWithRank(member.id());
        for (WaitingWithRank waitingWithRank : waitingWithRanks) {
            Waiting waiting = waitingWithRank.getWaiting();
            long rank = waitingWithRank.getRank() + 1;
            responses.add(new MyReservationResponse(
                    waiting.getId(),
                    waiting.getTheme().getName(),
                    waiting.getDate(),
                    waiting.getTime().getValue(),
                    rank + "번째 예약대기"
            ));
        }
        return responses;
    }

    public ReservationRequest withName(ReservationRequest reservationRequest, LoginMember member) {
        if (reservationRequest.getName() == null || reservationRequest.getName().isBlank()) {
            return new ReservationRequest(member.name(), reservationRequest.getDate(), reservationRequest.getTheme(), reservationRequest.getTime());
        }
        return reservationRequest;
    }


    public void deleteById(Long id, LoginMember member) {
        if (!Objects.equals(id, member.id()) && member.isUser()) {
            throw new IllegalArgumentException("자신의 예약만 삭제가능합니다.");
        }
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }
}
