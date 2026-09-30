package roomescape.reservation;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.login.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.WaitingRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final WaitingRepository waitingRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            MemberRepository memberRepository,
            ThemeRepository themeRepository,
            TimeRepository timeRepository,
            WaitingRepository waitingRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.waitingRepository = waitingRepository;
    }

    @Transactional
    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {

        boolean exists =
                reservationRepository
                        .existsByDateAndTimeIdAndThemeId(
                                reservationRequest.getDate(),
                                reservationRequest.getTime(),
                                reservationRequest.getTheme()
                        );

        if (exists) {
            throw new DuplicateReservationException();
        }

        Theme theme = themeRepository
                .findById(reservationRequest.getTheme())
                .orElseThrow(NoSuchElementException::new);

        Time time = timeRepository
                .findById(reservationRequest.getTime())
                .orElseThrow(NoSuchElementException::new);

        Reservation reservation;

        if (reservationRequest.getName() != null) {
            reservation = new Reservation(
                    null,
                    reservationRequest.getName(),
                    reservationRequest.getDate(),
                    time,
                    theme
            );
        } else {
            Member member = memberRepository
                    .findById(loginMember.id())
                    .orElseThrow(NoSuchElementException::new);

            reservation = new Reservation(
                    member,
                    "",
                    reservationRequest.getDate(),
                    time,
                    theme
            );
        }

        Reservation savedReservation;

        try {
            savedReservation = reservationRepository.saveAndFlush(reservation);
        }
        catch (DataIntegrityViolationException e) {
            if (isDuplicateReservationConstraint(e)) {
                throw new DuplicateReservationException();
            }

            throw e;
        }

        String reservationName;

        if (savedReservation.getMember() != null) {
            reservationName = savedReservation.getMember().getName();
        } else {
            reservationName = savedReservation.getName();
        }

        return new ReservationResponse(
                savedReservation.getId(),
                reservationName,
                savedReservation.getTheme().getName(),
                savedReservation.getDate(),
                savedReservation.getTime().getValue()
        );
    }

    public List<MyReservationResponse> findMyReservations(LoginMember loginMember) {
        List<MyReservationResponse> reservations =
                reservationRepository
                        .findAllByMemberId(loginMember.id())
                        .stream()
                        .map(reservation ->
                                new MyReservationResponse(
                                        reservation.getId(),
                                        reservation.getTheme().getName(),
                                        reservation.getDate(),
                                        reservation.getTime().getValue(),
                                        "예약"
                                )
                        )
                        .toList();

        List<MyReservationResponse> waitings =
                waitingRepository
                        .findWaitingsWithRankByMemberId(
                                loginMember.id()
                        )
                        .stream()
                        .map(waitingWithRank -> {
                            long rank = waitingWithRank.getRank() + 1;

                            return new MyReservationResponse(
                                    waitingWithRank.getId(),
                                    waitingWithRank.getTheme(),
                                    waitingWithRank.getDate(),
                                    waitingWithRank.getTime(),
                                    rank + "번째 예약대기"
                            );
                        })
                        .toList();

        return java.util.stream.Stream
                .concat(
                        reservations.stream(),
                        waitings.stream()
                )
                .toList();
    }

    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAllWithDetails()
                                    .stream()
                                    .map(reservation -> {
                                        String reservationName;

                                        if (reservation.getMember() != null) {
                                            reservationName = reservation.getMember().getName();
                                        }
                                        else {
                                            reservationName = reservation.getName();
                                        }

                                        return new ReservationResponse(
                                                reservation.getId(),
                                                reservationName,
                                                reservation.getTheme().getName(),
                                                reservation.getDate(),
                                                reservation.getTime().getValue()
                                        );
                                    })
                                    .toList();
    }

    private boolean isDuplicateReservationConstraint(DataIntegrityViolationException exception) {
        Throwable cause = exception;

        while (cause != null) {
            if (cause instanceof ConstraintViolationException constraintException) {
                String constraintName = constraintException.getConstraintName();

                return constraintName != null && constraintName.equalsIgnoreCase(
                        "uk_reservation_date_time_theme"
                );
            }

            cause = cause.getCause();
        }

        return false;
    }
}
