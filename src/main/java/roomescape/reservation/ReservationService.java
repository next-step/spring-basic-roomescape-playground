package roomescape.reservation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MyReservationResponse;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.Waiting;
import roomescape.waiting.WaitingRepository;
import roomescape.waiting.WaitingWithRank;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly=true)
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final WaitingRepository waitingRepository;

    public ReservationService(ReservationRepository reservationRepository, TimeRepository timeRepository, ThemeRepository themeRepository, WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.waitingRepository = waitingRepository;
    }

    @Transactional
    public ReservationResponse save(ReservationRequest reservationRequest, Member member) {
        Time time = timeRepository.findById(reservationRequest.getTime())
                .orElseThrow(() -> new IllegalArgumentException("시간을 찾을 수 없습니다"));
        Theme theme = themeRepository.findById(reservationRequest.getTheme())
                .orElseThrow(() -> new IllegalArgumentException("테마를 찾을 수 없습니다."));

        String reservationName;
        if (member != null) {
            reservationName = member.getName();
        } else {
            reservationName = reservationRequest.getName();
        }

        if (reservationName == null || reservationName.isEmpty()) {
            throw new IllegalArgumentException("예약자 이름을 찾을 수 없습니다.");
        }

        Reservation reservation = new Reservation(reservationName, reservationRequest.getDate(), time, theme, member);

        Reservation savedReservation = reservationRepository.save(reservation);

        return new ReservationResponse(reservation.getId(),
                savedReservation.getName(),
                savedReservation.getTheme().getName(),
                savedReservation.getDate(),
                savedReservation.getTime().getValue()
        );
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(),
                        it.getName(),
                        it.getTheme().getName(),
                        it.getDate(),
                        it.getTime().getValue()
                ))
                .toList();
    }

    public List<MyReservationResponse> findReservationsByMember(LoginMember loginMember) {
        List<MyReservationResponse> response = new ArrayList<>();

        List<Reservation> reservations = reservationRepository.findByMemberId(loginMember.getId());
        List<MyReservationResponse> reservationResponses = reservations.stream()                .map(it -> new MyReservationResponse(
                        it.getId(),
                        it.getTheme().getName(),
                        it.getDate(),
                        it.getTime().getValue(),
                        "예약"
                ))
                .collect(Collectors.toList());
        response.addAll(reservationResponses);

        List<WaitingWithRank> waitings = waitingRepository.findWaitingsWithRankByMemberId(loginMember.getId());
        List<MyReservationResponse> waitingResponses = waitings.stream()
                .map(it -> {
                    Waiting w = it.getWaiting();
                    long rank = it.getRank() + 1;
                    return new MyReservationResponse(
                            w.getId(),
                            w.getTheme().getName(),
                            w.getDate(),
                            w.getTime().getValue(),
                            rank + "번째 예약대기"
                    );
                })
                .toList();
        response.addAll(waitingResponses);

        return response;
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
}
