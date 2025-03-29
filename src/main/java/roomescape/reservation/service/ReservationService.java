package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import roomescape.auth.dto.LoginMember;
import roomescape.exception.BadRequestException;
import roomescape.exception.ExceptionMessage;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.request.ReservationRequest;
import roomescape.reservation.dto.response.MyReservationResponse;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.Time;
import roomescape.time.repository.TimeRepository;
import roomescape.waiting.domain.WaitingWithRank;
import roomescape.waiting.repository.WaitingRepository;

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
                .map(ReservationResponse::new)
                .toList();
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        Reservation reservation = createReservation(reservationRequest, loginMember);
        Reservation reservationWithId = reservationRepository.save(reservation);
        return new ReservationResponse(reservationWithId);
    }

    private Reservation createReservation(ReservationRequest reservationRequest, LoginMember loginMember) {
        Time time = findTime(reservationRequest.getTime());
        Theme theme = findTheme(reservationRequest.getTheme());

        if (reservationRequest.isInvalidName()) {
            return reservationRequest.toReservationByMember(loginMember, time, theme);
        }
        return reservationRequest.toReservationByAdmin(time, theme);
    }

    private Time findTime(long timeId) {
        return timeRepository.findById(timeId)
                .orElseThrow(() -> new BadRequestException(ExceptionMessage.INVALID_TIME.getMessage()));
    }

    private Theme findTheme(long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new BadRequestException(ExceptionMessage.INVALID_THEME.getMessage()));
    }

    public List<MyReservationResponse> findMyReservations(LoginMember loginMember) {
        List<Reservation> reservations = reservationRepository.findByMemberId(loginMember.id());
        List<WaitingWithRank> waitings = waitingRepository.findWaitingsWithRankByMemberId(loginMember.id());

        return Stream.concat(
                reservations.stream().map(MyReservationResponse::new),
                waitings.stream().map(MyReservationResponse::new))
                .toList();
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
}
