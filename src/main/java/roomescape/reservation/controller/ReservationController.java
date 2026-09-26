package roomescape.reservation.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.auth.AdminOnly;
import roomescape.member.domain.LoginMember;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dto.MyReservationResponse;
import roomescape.reservation.service.ReservationCommand;
import roomescape.reservation.service.ReservationResult;
import roomescape.reservation.service.MyReservationResult;
import roomescape.reservation.service.ReservationService;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    @AdminOnly
    public List<ReservationResponse> list() {
        return reservationService.findAll().stream()
                .map(this::toReservationResponse)
                .toList();
    }

    @GetMapping("/reservations-mine")
    public List<MyReservationResponse> listMine(LoginMember loginMember) {
        return reservationService.findMine(loginMember).stream()
                .map(this::toMyReservationResponse)
                .toList();
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> create(@Valid @RequestBody ReservationRequest reservationRequest,
                                                      LoginMember loginMember) {
        ReservationResult reservation = reservationService.save(toCommand(reservationRequest), loginMember);

        return ResponseEntity.created(URI.create("/reservations/" + reservation.id()))
                .body(toReservationResponse(reservation));
    }

    @DeleteMapping("/reservations/{id}")
    @AdminOnly
    public ResponseEntity<Void> delete(@PathVariable Long id, LoginMember loginMember) {
        reservationService.deleteById(id, loginMember);
        return ResponseEntity.noContent().build();
    }

    private MyReservationResponse toMyReservationResponse(MyReservationResult result) {
        String status = switch (result.status()) {
            case RESERVED -> "예약";
            case WAITING -> result.waitingRank() + "번째 예약대기";
        };
        return new MyReservationResponse(
                result.id(),
                result.theme(),
                result.date(),
                result.time(),
                status
        );
    }

    private ReservationCommand toCommand(ReservationRequest request) {
        return new ReservationCommand(
                request.memberId(),
                request.name(),
                request.date(),
                request.themeId(),
                request.timeId()
        );
    }

    private ReservationResponse toReservationResponse(ReservationResult result) {
        return new ReservationResponse(
                result.id(),
                result.name(),
                result.theme(),
                result.date(),
                result.time()
        );
    }
}
