package roomescape.reservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

        if (!inventory.getReservations().isEmpty()) throw new AlreadyBookedTimeReservationException();
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
        int waitingNumber = reservation.refreshStatus();
        reservationRepository.save(reservation);

        return new WaitingResponse(waitingNumber);
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

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getMember().getId(), it.getMember().getName(), it.getInventory().getTheme().getName(), it.getInventory().getDate().getValue(), it.getInventory().getTime().getValue()))
                .toList();
    }

    public List<MyReservationResponse> getMyReservations(Long memberId) {
        Member member = memberService.loadMemberEntity(memberId);
        List<Reservation> myReservations = reservationRepository.findByMember(member);

        for(Reservation myReservation:myReservations) {
            myReservation.refreshStatus();
        }

        return myReservations.stream()
                .map(reservation -> new MyReservationResponse(
                        reservation.getId(),
                        reservation.getInventory().getTheme().getName(),
                        reservation.getInventory().getDate().getValue(),
                        reservation.getInventory().getTime().getValue(),
                        reservation.getStatus().toString())
                )
                .toList();
    }
}
