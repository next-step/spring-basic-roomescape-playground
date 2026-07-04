package roomescape.reservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.exception.NoSuchReservationException;
import roomescape.reservation.model.Date;
import roomescape.member.model.Member;
import roomescape.member.service.MemberService;
import roomescape.reservation.dto.WaitingResponse;
import roomescape.reservation.exception.AlreadyBookedTimeReservationException;
import roomescape.reservation.exception.NoSuchInventoryException;
import roomescape.reservation.model.Inventory;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.repository.InventoryRepository;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.model.ReservationStatus;
import roomescape.reservation.dto.MyReservationResponse;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.theme.model.Theme;
import roomescape.theme.service.ThemeService;
import roomescape.time.model.AvailableTime;
import roomescape.time.model.Time;
import roomescape.time.service.TimeService;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    private final InventoryRepository inventoryRepository;
    private final ReservationRepository reservationRepository;
    private final MemberService memberService;
    private final ThemeService themeService;
    private final TimeService timeService;

    @Autowired
    public ReservationService(InventoryRepository inventoryRepository, ReservationRepository reservationRepository, MemberService memberService, ThemeService themeService, TimeService timeService) {
        this.inventoryRepository = inventoryRepository;
        this.reservationRepository = reservationRepository;
        this.memberService = memberService;
        this.themeService = themeService;
        this.timeService = timeService;
    }


    @Transactional
    public ReservationResponse registerReservation(Long memberId, ReservationRequest reservationRequest) {
        Member member = memberService.loadMemberEntity(memberId);

        Date date = new Date(reservationRequest.date());
        Time time = timeService.loadTimeEntity(reservationRequest.timeId());
        Theme theme = themeService.loadThemeEntity(reservationRequest.themeId());
        Inventory inventory = inventoryRepository.findByDateAndTimeAndTheme(date, time, theme)
                .orElseGet(() -> inventoryRepository.save(new Inventory(date, time, theme)));

        if (reservationRepository.existsByInventoryAndStatus(inventory, ReservationStatus.CONFIRMED)) throw new AlreadyBookedTimeReservationException();
        ReservationStatus status = ReservationStatus.CONFIRMED;

        Reservation reservation = new Reservation(member, inventory, status);
        reservationRepository.save(reservation);

        return new ReservationResponse(
                reservation.getId(),
                reservation.getMember().getId(),
                reservation.getMember().getName(),
                reservation.getInventory().getTheme().getName(),
                reservation.getInventory().getDate().getValue(),
                reservation.getInventory().getTime().getValue()
        );
    }

    @Transactional
    public WaitingResponse waitReservation(Long memberId, ReservationRequest reservationRequest) {
        Member member = memberService.loadMemberEntity(memberId);

        Date date = new Date(reservationRequest.date());
        Time time = timeService.loadTimeEntity(reservationRequest.timeId());
        Theme theme = themeService.loadThemeEntity(reservationRequest.themeId());
        Inventory inventory = inventoryRepository.findByDateAndTimeAndTheme(date, time, theme).orElseThrow(NoSuchInventoryException::new);

        ReservationStatus status = ReservationStatus.PENDING;

        Reservation reservation = new Reservation(member, inventory, status);
        Reservation savedReservation = reservationRepository.save(reservation);
        Long waitingNumber = getWaitingNumber(savedReservation);

        return new WaitingResponse(savedReservation.getId(), waitingNumber);
    }

    public List<AvailableTime> getAvailableTime(String dateValue, Long themeId) {
        Date date = new Date(dateValue);
        Theme theme = themeService.loadThemeEntity(themeId);
        List<Inventory> inventories = inventoryRepository.findByDateAndTheme(date, theme);
        List<Time> times = timeService.findAll();

        return times.stream()
                .map(time -> new AvailableTime(
                        time.getId(),
                        time.getValue(),
                        inventories.stream()
                                .anyMatch(inventory -> inventory.getTime().equals(time) && !inventory.getReservations().isEmpty())
                ))
                .toList();
    }

    @Transactional
    public void deleteReservationById(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(NoSuchReservationException::new);
        Inventory inventory = reservation.getInventory();

        Optional<Reservation> nextOptionalReservation = reservationRepository.findFirstByInventoryAndStatus(inventory, ReservationStatus.PENDING);
        nextOptionalReservation.ifPresent(nextReservation -> nextReservation.setStatus(ReservationStatus.CONFIRMED));

        reservationRepository.delete(reservation);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getMember().getId(), it.getMember().getName(), it.getInventory().getTheme().getName(), it.getInventory().getDate().getValue(), it.getInventory().getTime().getValue()))
                .toList();
    }

    public List<MyReservationResponse> getMyReservations(Long memberId) {
        List<Reservation> myReservations = reservationRepository.findDetailedReservations(memberId);

        return myReservations.stream()
                .map(reservation -> new MyReservationResponse(
                        reservation.getId(),
                        reservation.getInventory().getTheme().getName(),
                        reservation.getInventory().getDate().getValue(),
                        reservation.getInventory().getTime().getValue(),
                        getReservationStatus(reservation))
                )
                .toList();
    }

    private String getReservationStatus(Reservation reservation) {
        if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
            return reservation.getStatus().toString();
        }

        return getWaitingNumber(reservation) + "번째 예약대기";
    }

    private Long getWaitingNumber(Reservation reservation) {
        return reservationRepository.countEarlierReservations(
                reservation.getInventory().getId(),
                ReservationStatus.PENDING,
                reservation.getId()
        ) + 1;
    }
}
