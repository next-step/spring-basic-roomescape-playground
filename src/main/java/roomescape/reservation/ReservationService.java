package roomescape.reservation;

import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.WaitingRepository;
import roomescape.waiting.WaitingResponse;
import roomescape.waiting.WaitingWithRank;

import java.lang.invoke.CallSite;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
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

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getTime()))
                .toList();
    }

    public List<MyReservationResponse> findMyReservationsAll(Member loginMember) {

        List<MyReservationResponse> myReservationResponses = reservationRepository.findByName(loginMember.getName()).stream()
                .map(it -> new MyReservationResponse(
                        it.getId(),
                        it.getTheme().getName(),
                        it.getDate(),
                        it.getTime().getTime(),
                        "예약"))
                .toList();

        List<WaitingWithRank> waitingWithRanks = waitingRepository.findWaitingWithRankByMemberId(loginMember.getId());

        List<MyReservationResponse> myReservationResponsesFromWaiting = waitingWithRanks.stream()
                .map((it -> new MyReservationResponse(
                        it.getWaiting().getId(),
                        it.getWaiting().getTheme().getName(),
                        it.getWaiting().getDate(),
                        it.getWaiting().getTime().getTime(),
                        it.getRank() + 1 + "번째 예약대기"
                )))
                .toList();

        return Stream.concat(myReservationResponses.stream(),
                myReservationResponsesFromWaiting.stream())
                .toList();
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Time time = timeRepository.findById(Long.parseLong(reservationRequest.getTime()))
                .orElseThrow(() -> new IllegalArgumentException("해당 시간이 존재하지 않습니다."));

        Theme theme = themeRepository.findById(Long.parseLong(reservationRequest.getTheme()))
                .orElseThrow(() -> new IllegalArgumentException("해당 테마가 존재하지 않습니다."));

        Reservation reservation = reservationRepository.save(new Reservation(
                reservationRequest.getName(),
                reservationRequest.getDate(),
                time,
                theme,
                null));

        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getTime());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
}
