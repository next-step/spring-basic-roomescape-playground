package roomescape.reservation.admin;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminReservationController {
    private final AdminReservationService adminReservationService;

    public AdminReservationController(AdminReservationService adminReservationService) {
        this.adminReservationService = adminReservationService;
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity createAdminReservation(@RequestBody AdminReservationRequest adminReservationRequest) {
        if (adminReservationRequest.getDate() == null
                || adminReservationRequest.getEmail() == null
                || adminReservationRequest.getTheme() == null
                || adminReservationRequest.getTime() == null) {
            return ResponseEntity.badRequest().build();
        }

        AdminReservationResponse reservation = adminReservationService.saveAdminReservation(adminReservationRequest);

        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }

    @GetMapping("/admin/reservations")
    public List<AdminReservationResponse> list() {
        System.out.println("this");
        return adminReservationService.findAll();
    }
}
