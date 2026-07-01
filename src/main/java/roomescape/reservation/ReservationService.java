package roomescape.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.Member;
import roomescape.member.MemberService;
import roomescape.theme.Theme;
import roomescape.theme.ThemeService;
import roomescape.time.AvailableTime;
import roomescape.time.Time;
import roomescape.time.TimeService;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final FailedReservationService failedReservationService;
    private final MemberService memberService;
    private final ThemeService themeService;
    private final TimeService timeService;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, FailedReservationService failedReservationService, MemberService memberService, ThemeService themeService, TimeService timeService) {
        this.reservationRepository = reservationRepository;
        this.failedReservationService = failedReservationService;
        this.memberService = memberService;
        this.themeService = themeService;
        this.timeService = timeService;
    }

    @Transactional
    public ReservationResponse registerReservation(String memberName, ReservationRequest reservationRequest) {
        Member member = memberService.loadMemberEntity(memberName);
        Theme theme = themeService.findEntityById(reservationRequest.themeId());
        Time time = timeService.findEntityById(reservationRequest.timeId());
        Reservation reservation = new Reservation(reservationRequest.name(), reservationRequest.date(), time, theme, member);

        if (!isAvailable(time, reservationRequest.date(), reservationRequest.themeId())) {
            failedReservationService.registerFailedReservation(reservation);
            throw new AlreadyBookedTimeReservationException();
        } else {
            reservation.setStatus(ReservationStatus.CONFIRMED);
            reservationRepository.save(reservation);
        }

        return new ReservationResponse(reservation.getId(), reservationRequest.name(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    public List<AvailableTime> getAvailableTime(String date, Long themeId) {
        Theme theme = themeService.findEntityById(themeId);
        List<Reservation> reservations = reservationRepository.findByDateAndTheme(date, theme);
        List<Time> times = timeService.findAll();

        return times.stream()
                .map(time -> new AvailableTime(
                        time.getId(),
                        time.getValue(),
                        reservations.stream()
                                .anyMatch(reservation -> reservation.getTime().getId().equals(time.getId()))
                ))
                .toList();
    }

    private boolean isAvailable(Time time, String date, Long themeId) {
        Long timeId = time.getId();
        List<AvailableTime> availableTimes = getAvailableTime(date, themeId);
        return availableTimes.stream()
                .anyMatch(availableTime -> availableTime.getTimeId().equals(timeId) && !availableTime.isBooked());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    public List<MyReservationResponse> getMyReservations(String memberName) {
        List<Reservation> reservations = reservationRepository.findByName(memberName);

        return reservations.stream()
                .map(reservation -> new MyReservationResponse(
                        reservation.getId(),
                        reservation.getTheme().getName(),
                        reservation.getDate(),
                        reservation.getTime().getValue(),
                        reservation.getStatus() == null ? "예약" : reservation.getStatus().toString()))
                .toList();
    }
}
