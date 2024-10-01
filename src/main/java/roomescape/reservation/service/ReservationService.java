package roomescape.reservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;

import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.Waiting;
import roomescape.reservation.dto.WaitingWithRank;
import roomescape.reservation.dto.request.ReservationRequest;
import roomescape.reservation.dto.request.WaitingRequest;
import roomescape.reservation.dto.response.MyReservationResponse;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.dto.response.WaitingResponse;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.WaitingRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.Time;
import roomescape.time.repository.TimeRepository;

@Service
@Transactional(readOnly = true)
public class ReservationService {
    private ReservationRepository reservationRepository;
    private TimeRepository timeRepository;
    private ThemeRepository themeRepository;
    private WaitingRepository waitingRepository;

    @Autowired
    public ReservationService(
        ReservationRepository reservationRepository,
        TimeRepository timeRepository,
        ThemeRepository themeRepository,
        WaitingRepository waitingRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.waitingRepository = waitingRepository;
    }

    @Transactional
    public ReservationResponse saveReservation(ReservationRequest reservationRequest) {
        Time time = timeRepository.findById(reservationRequest.getTime())
            .orElseThrow(EntityNotFoundException::new);

        Theme theme = themeRepository.findById(reservationRequest.getTheme())
            .orElseThrow(EntityNotFoundException::new);

        Reservation reservation = reservationRepository.save(
            new Reservation(
                reservationRequest.getMember(),
                reservationRequest.getName(),
                reservationRequest.getDate(),
                time,
                theme
            )
        );

        return new ReservationResponse(
            reservation.getId(),
            reservationRequest.getName(),
            reservation.getTheme().getName(),
            reservation.getDate(),
            reservation.getTime().getValue()
        );
    }

    @Transactional
    public WaitingResponse saveWaiting(WaitingRequest waitingRequest, Member member) {
        Time time = timeRepository.getById(waitingRequest.time());

        Theme theme = themeRepository.getById(waitingRequest.theme());

        Integer waitingNumber = waitingRepository.findWaitingRank(theme, waitingRequest.date(), time);

        Waiting waiting = waitingRepository.save(
            new Waiting(member, member.getName(), waitingRequest.date(), time, theme)
        );

        return WaitingResponse.from(waiting, waitingNumber);
    }

    @Transactional
    public void deleteReservationById(Long id) {
        reservationRepository.deleteById(id);
    }

    @Transactional
    public void deleteWaitingById(Long waitingId) {
        waitingRepository.deleteById(waitingId);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    public List<MyReservationResponse> findReservationsByMember(Long memberId) {
        List<Reservation> reservations = reservationRepository.findByMemberId(memberId);
        List<WaitingWithRank> waiting = waitingRepository.findWaitingsWithRankByMemberId(memberId);

        return MyReservationResponse.from(reservations, waiting);
    }

    public boolean checkWaitingExist(WaitingRequest waitingRequest, Member member) {
        Time time = timeRepository.findById(waitingRequest.time())
            .orElseThrow(EntityNotFoundException::new);

        Theme theme = themeRepository.findById(waitingRequest.theme())
            .orElseThrow(EntityNotFoundException::new);

        return waitingRepository.findExistWaiting(waitingRequest.date(), theme, time, member).isPresent();
    }

    public boolean checkReservationExist(ReservationRequest reservationRequest) {
        Time time = timeRepository.findById(reservationRequest.getTime())
            .orElseThrow(EntityNotFoundException::new);

        Theme theme = themeRepository.findById(reservationRequest.getTheme())
            .orElseThrow(EntityNotFoundException::new);

        return reservationRepository.findExistReservation(
            reservationRequest.getDate(),
            theme,
            time,
            reservationRequest.getMember()
        ).isPresent();
    }
}
