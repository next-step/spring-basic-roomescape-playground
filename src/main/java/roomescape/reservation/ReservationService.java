package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.WaitingRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ReservationService {
    private ReservationRepository reservationRepository;
    private TimeRepository timeRepository;
    private ThemeRepository themeRepository;
    private WaitingRepository waitingRepository;


    public ReservationService(ReservationRepository reservationRepository, TimeRepository timeRepository, ThemeRepository themeRepository, WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.waitingRepository = waitingRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Time time = timeRepository.findById(reservationRequest.getTime()).orElseThrow();
        Theme theme = themeRepository.findById(reservationRequest.getTheme()).orElseThrow();

        reservationRepository.findByDateAndTimeAndThemeId(reservationRequest.getDate(), time, reservationRequest.getTheme())
                .stream()
                .findFirst()
                .ifPresent(it -> {
                    throw new IllegalArgumentException("이미 예약된 시간입니다.");
                });

        Reservation reservation = reservationRepository.save(new Reservation(reservationRequest.getName(), reservationRequest.getDate(), time, theme));
        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }


    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }
    public List<MyReservationResponse> findMyReservations(Long memberId) {
         List<MyReservationResponse> myReservationResponseList = reservationRepository.findByMemberId(memberId).stream()
                .map(it -> new MyReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
         List<MyReservationResponse> myWaitingResponseList = waitingRepository.findWaitingsWithRankByMemberId(memberId).stream()
                 .map(it -> new MyReservationResponse(it.getWaiting().getId(), it.getWaiting().getTheme().getName(), it.getWaiting().getDate(), it.getWaiting().getTime(), (it.getRank()+1) + "번째 예약대기"))
                 .toList();

         return Stream.concat(myReservationResponseList.stream(), myWaitingResponseList.stream())
                 .sorted(Comparator.comparing(MyReservationResponse::getDate).thenComparing(MyReservationResponse::getTime)).toList();

    }

}
