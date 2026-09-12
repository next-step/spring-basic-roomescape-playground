package roomescape.domain.reservation.web.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.auth.principal.LoginMember;
import roomescape.domain.auth.web.support.AdminOnly;
import roomescape.domain.auth.web.support.Login;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.service.ReservationService;
import roomescape.domain.reservation.web.dto.ReservationRequest;
import roomescape.domain.reservation.web.dto.ReservationResponse;
import roomescape.global.exception.ForbiddenException;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationController {

    private final Logger log =  LoggerFactory.getLogger(ReservationController.class);

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> list() {
        return reservationService.findAll().stream().map(ReservationResponse::from).toList();
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> create(
            @Valid @RequestBody ReservationRequest reservationRequest,
            @Login LoginMember loginMember
    ) {
        String username = reservationRequest.name();

        if (username == null || username.isBlank()) {
            username = loginMember.getName();
        } else if (!loginMember.isAdmin()) {
            throw new ForbiddenException();
        }

        Reservation newReservation = reservationService.save(username, reservationRequest.date(), reservationRequest.theme(), reservationRequest.time());

        return ResponseEntity.created(URI.create("/reservations/" + newReservation.getId())).body(ReservationResponse.from(newReservation));
    }

    // NOTE: ID 삭제 등 소유권이 불분명한 예약 취소이므로, 관리자만 삭제할 수 있음을 명시합니다.
    @AdminOnly
    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
