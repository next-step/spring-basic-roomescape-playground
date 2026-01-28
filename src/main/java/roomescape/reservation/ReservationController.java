package roomescape.reservation;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.member.LoginMemberDto;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationController {

    private final ReservationService reservationService;
    private final MyReservationService myReservationService;

    public ReservationController(ReservationService reservationService, MyReservationService myReservationService) {
        this.reservationService = reservationService;
        this.myReservationService = myReservationService;
    }

    @GetMapping("/reservations")
    public List<ReservationResponseDto> list() {
        return reservationService.findAll();
    }

    @GetMapping("/admin/reservations")
    public List<ReservationResponseDto> adminList() {
        return reservationService.findAll();
    }

    @PostMapping("/reservations")
    public ResponseEntity create(@RequestBody @Valid ReservationRequestDto reservationRequest, LoginMemberDto member) {

        String effectiveName = reservationRequest.name() != null && !reservationRequest.name().isBlank()
                ? reservationRequest.name()
                : (member != null ? member.name() : null);
        if (effectiveName == null || effectiveName.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        ReservationRequestDto requestWithName = new ReservationRequestDto(
                effectiveName,
                reservationRequest.date(),
                reservationRequest.theme(),
                reservationRequest.time()
        );

        ReservationResponseDto reservation = reservationService.save(
                requestWithName,
                member != null ? member.id() : null
        );

        return ResponseEntity.created(URI.create("/reservations/" + reservation.id())).body(reservation);
    }

    @PostMapping("/admin/reservations")
    public ResponseEntity adminCreate(@RequestBody @Valid ReservationRequestDto reservationRequest) {
        if (reservationRequest.name() == null || reservationRequest.name().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        ReservationResponseDto reservation = reservationService.save(reservationRequest, null);
        return ResponseEntity.created(URI.create("/admin/reservations/" + reservation.id())).body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/admin/reservations/{id}")
    public ResponseEntity adminDelete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservations-mine")
    public ResponseEntity<List<MyReservationResponseDto>> mine(LoginMemberDto member) {
        return ResponseEntity.ok(myReservationService.findMine(member.id()));
    }
}
