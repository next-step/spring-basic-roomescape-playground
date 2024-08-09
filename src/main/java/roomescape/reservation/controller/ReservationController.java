package roomescape.reservation.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.global.login.LoginMemberName;
import roomescape.reservation.controller.dto.MyReservationResponse;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.controller.dto.ReservationRequest;
import roomescape.reservation.controller.dto.ReservationResponse;

import java.util.List;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> list() {
        return reservationService.findAll();
    }

    @GetMapping("/reservations-mine")
    public List<MyReservationResponse> mine(@LoginMemberName String memberName) {
        return reservationService.findMyReservations(memberName);
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> create(@LoginMemberName String memberName,
                                                      @RequestBody ReservationRequest reservationRequest) {
        if (isNamePresentInRequest(reservationRequest)) {
            memberName = reservationRequest.name();
        }
        if (isNotNullRequestParameters(memberName, reservationRequest)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        ReservationResponse reservation = reservationService.save(memberName, reservationRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.LOCATION, "/reservations/" + reservation.id())
                .body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    private boolean isNamePresentInRequest(ReservationRequest reservationRequest) {
        return reservationRequest.name() != null;
    }

    private boolean isNotNullRequestParameters(String memberName, ReservationRequest reservationRequest) {
        return memberName == null
                || reservationRequest.date() == null
                || reservationRequest.theme() == null
                || reservationRequest.time() == null;
    }
}
