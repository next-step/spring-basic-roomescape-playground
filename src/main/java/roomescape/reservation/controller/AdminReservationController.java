package roomescape.reservation.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.domain.LoginMember;
import roomescape.reservation.dto.AdminReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.service.ReservationCreateService;
import roomescape.reservation.service.ReservationFindService;

@RestController
public class AdminReservationController {
    private final ReservationCreateService createService;
    private final ReservationFindService findService;

    public AdminReservationController(ReservationCreateService createService, ReservationFindService findService) {
        this.createService = createService;
        this.findService = findService;
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity<ReservationResponse> createAdminReservation(@Valid @RequestBody AdminReservationRequest request, LoginMember loginMember) {
        ReservationResponse reservation = createService.saveAdminReservation(request, loginMember);

        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId()))
                .body(reservation);
    }

    @GetMapping("/admin/reservations")
    public List<ReservationResponse> findAllMemberReservations(LoginMember loginMember) {
        return findService.findReservations(loginMember);
    }
}
