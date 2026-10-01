package roomescape.domain.reservation.web.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.auth.principal.LoginMember;
import roomescape.domain.auth.web.support.annotation.AdminOnly;
import roomescape.domain.auth.web.support.annotation.Login;
import roomescape.domain.auth.web.support.annotation.LoginRequired;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.service.ReservationService;
import roomescape.domain.reservation.web.dto.MyReservationsResponse;
import roomescape.domain.reservation.web.dto.ReservationRequest;
import roomescape.domain.reservation.web.dto.ReservationResponse;
import roomescape.domain.waiting.service.ReserveWaitingService;
import roomescape.domain.waiting.service.result.WaitingWithRank;
import roomescape.global.exception.BadRequestException;
import roomescape.global.exception.ConflictException;

import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@RestController
public class ReservationController {

    private final Logger log = LoggerFactory.getLogger(ReservationController.class);

    private final ReservationService reservationService;
    private final ReserveWaitingService reserveWaitingService;

    public ReservationController(ReservationService reservationService,  ReserveWaitingService reserveWaitingService) {
        this.reservationService = reservationService;
        this.reserveWaitingService = reserveWaitingService;
    }

    @AdminOnly
    @GetMapping("/reservations")
    public List<ReservationResponse> list() {
        return reservationService.findAll().stream().map(ReservationResponse::from).toList();
    }

    @LoginRequired
    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> create(
            @Valid @RequestBody ReservationRequest request,
            @Login LoginMember loginMember
    ) {
        Reservation newReservation;

        try {
            if (loginMember.isAdmin()) {
                newReservation = reserveByAdmin(loginMember, request);
            } else {
                newReservation = reserveByUser(loginMember, request);
            }
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("이미 예약이 존재합니다.");
        }
        return ResponseEntity.created(URI.create("/reservations/" + newReservation.getId())).body(ReservationResponse.from(newReservation));
    }

    // NOTE: ID 삭제 등 소유권이 불분명한 예약 취소이므로, 관리자만 삭제할 수 있음을 명시합니다.
    @AdminOnly
    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @Login LoginMember loginMember
    ) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @LoginRequired
    @GetMapping("/reservations-mine")
    public List<MyReservationsResponse> getAllMyReservation(
            @Login LoginMember loginMember
    ) {
        List<Reservation> reservations = reservationService.findAllReservationByUser(loginMember.id());
        List<WaitingWithRank> reserveWaits = reserveWaitingService.findAllMemberReserveWaits(loginMember.id());

        return Stream.concat(
                reservations.stream().map(MyReservationsResponse::from),
                reserveWaits.stream().map(MyReservationsResponse::from)
        ).sorted(
                Comparator.comparing(MyReservationsResponse::date)
                        .thenComparing(MyReservationsResponse::time)
        ).toList();
    }

    private Reservation reserveByAdmin(LoginMember loginMember, ReservationRequest request) {
        if (!StringUtils.hasText(request.name())) {
            throw new BadRequestException("name 필드는 필수값입니다.");
        }
        return reservationService.createReservation(request.name(), request.date(), request.theme(), request.time());
    }

    private Reservation reserveByUser(LoginMember loginMember, ReservationRequest request) {
        if (StringUtils.hasText(request.name())) {
            log.warn("[ReservationController.reserveByUser] 일반 사용자(id={})가 다른 사용자 명의(name={})로 예약을 시도했습니다.",
                    loginMember.id(), request.name());
            throw new BadRequestException("HTTP 요청 바디의 형식이 잘못되었습니다.");
        }
        return reservationService.createReservation(loginMember.name(), request.date(), request.theme(), request.time());
    }
}
