package roomescape.reservation;

import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import roomescape.application.DuplicateReservationException;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.WaitingRepository;
import roomescape.waiting.WaitingWithRank;

import java.util.List;
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

    public void checkReservationRequest(ReservationRequest reservationRequest) {
        if (reservationRepository.existsByDateAndThemeIdAndTimeId(reservationRequest.getDate(),
                Long.parseLong(reservationRequest.getTheme()),
                Long.parseLong(reservationRequest.getTheme()))) {
            throw new DuplicateReservationException("해당 예약은 이미 예약되어 있습니다.");
        }
    }

    public void checkNameExistence(ReservationRequest reservationRequest, Member loginMember) {
        if (reservationRequest.getName() == null) {
            reservationRequest.setName(loginMember.getName());
        }
    }

    public void validateReservationRequest(ReservationRequest reservationRequest) {
        if (reservationRequest.getName() == null)
            throw new IllegalArgumentException("해당 이름을 가진 사용자가 존재하지 않습니다.");
        if (reservationRequest.getDate() == null)
            throw new IllegalArgumentException("해당 날짜가 존재하지 않습니다.");
        if (reservationRequest.getTheme() == null)
            throw new IllegalArgumentException("해당 테마가 존재하지 않습니다.");
        if (reservationRequest.getTime() == null) {
            throw new IllegalArgumentException("해당 시간이 존재하지 않습니다.");
        }
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
}
