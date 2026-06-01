package roomescape.reservation;

import java.util.NoSuchElementException;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.Waiting;
import roomescape.reservation.dto.MyReservationResponse;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;

import java.util.List;
import roomescape.reservation.dto.WaitingRequest;
import roomescape.reservation.dto.WaitingResponse;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.WaitingRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.Time;
import roomescape.time.repository.TimeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final WaitingRepository waitingRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
            TimeRepository timeRepository, ThemeRepository themeRepository,
            WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.waitingRepository = waitingRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, Member member) {
        Time time = timeRepository.findById(reservationRequest.timeId())
                .orElseThrow(() -> new NoSuchElementException("time not found"));
        Theme theme = themeRepository.findById(reservationRequest.themeId())
                .orElseThrow(() -> new NoSuchElementException("theme not found"));

        Reservation reservation = reservationRepository.save(
                new Reservation(
                        reservationRequest.name(),
                        reservationRequest.date(),
                        time,
                        theme,
                        member
                )
        );

        return new ReservationResponse(reservation.getId(), member.getName(),
                reservation.getTheme().getName(), reservation.getDate(),
                reservation.getTime().getValue());
    }

    public WaitingResponse saveWaiting(WaitingRequest waitingRequest, Member member) {
        Time time = timeRepository.findById(waitingRequest.timeId())
                .orElseThrow(() -> new NoSuchElementException("time not found"));
        Theme theme = themeRepository.findById(waitingRequest.themeId())
                .orElseThrow(() -> new NoSuchElementException("theme not found"));

        Waiting waiting = waitingRepository.save(
                new Waiting(
                        waitingRequest.date(),
                        time,
                        theme,
                        member
                )
        );

        long rank = waitingRepository.countByDateAndTimeIdAndThemeIdAndIdLessThan(
                waitingRequest.date(),
                waitingRequest.timeId(),
                waitingRequest.themeId(),
                waiting.getId());

        return new WaitingResponse(
                waiting.getId(),
                waiting.getTheme().getName(),
                waiting.getDate(),
                waiting.getTime().getValue(),
                rank
        );
    }

    public List<MyReservationResponse> findByMember(Member member) {

        Stream<MyReservationResponse> reservations = reservationRepository.findByMemberId(
                        member.getId()).stream()
                .map(MyReservationResponse::ofReservation);

        Stream<MyReservationResponse> waitings = waitingRepository.findByMemberId(
                        member.getId()).stream()
                .map(w -> {
                    long rank = waitingRepository.countByDateAndTimeIdAndThemeIdAndIdLessThan(
                            w.getDate(), w.getTime().getId(), w.getTheme().getId(), w.getId()) + 1;
                    return MyReservationResponse.ofWaiting(w, rank);
                });

        return Stream.concat(reservations, waitings).toList();
    }

    public void deleteById(Long id, Member member) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("reservation not found"));

        if (member.getRole() != Role.ADMIN && !reservation.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("본인의 예약만 취소할 수 있습니다.");
        }

        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(),
                        it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    public void deleteWaitingById(Long id, Member member) {
        Waiting waiting = waitingRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("waiting not found"));

        if (member.getRole() != Role.ADMIN && !waiting.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("본인의 예약대기만 취소할 수 있습니다.");
        }
        waitingRepository.deleteById(id);
    }
}
