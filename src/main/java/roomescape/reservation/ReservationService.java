package roomescape.reservation;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
public class ReservationService {

    private static final int MAX_RESERVED_COUNT = 1;

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(
        ReservationRepository reservationRepository,
        MemberRepository memberRepository,
        TimeRepository timeRepository,
        ThemeRepository themeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse save(Long memberId, ReservationRequest reservationRequest) {
        Member member = getMember(memberId);
        Time time = getTime(reservationRequest.getTime());
        Theme theme = getTheme(reservationRequest.getTheme());

        validateDuplicateReservation(memberId, reservationRequest.getDate(), time, theme);

        Reservation reservation = new Reservation(
            member.getName(),
            reservationRequest.getDate(),
            time,
            theme,
            member,
            determineReservationStatus(reservationRequest.getDate(), time, theme)
        );

        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponse.from(savedReservation);
    }

    public void cancel(Long id) {
        Reservation reservation = reservationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                "not found reservation with id:" + id));
        boolean wasReserved = reservation.isReserved();

        reservationRepository.deleteById(id);

        if (wasReserved) {
            promoteWaitingToReserved(reservation);
        }
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
            .map(it -> new ReservationResponse(
                it.getId(),
                it.getName(),
                it.getTheme().getName(),
                it.getDate(),
                it.getTime().getValue()
            )).toList();
    }

    public List<MyReservationResponse> findMyReservations(Long memberId) {
        List<Reservation> myReservations =
            reservationRepository.findByMemberIdWithThemeAndTime(memberId);

        return myReservations.stream()
            .map(reservation -> {
                Integer rank = reservation.isWaiting() ? calculateWaitingRank(reservation) : null;
                return MyReservationResponse.from(reservation, rank);
            })
            .toList();
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(
                () -> new IllegalArgumentException("not found member with id: " + memberId));
    }

    private Time getTime(Long timeId) {
        return timeRepository.findById(timeId)
            .orElseThrow(() -> new IllegalArgumentException("not found time with id: " + timeId));
    }

    private Theme getTheme(Long themeId) {
        return themeRepository.findById(themeId)
            .orElseThrow(() -> new IllegalArgumentException("not found theme with id: " + themeId));
    }

    private ReservationStatus determineReservationStatus(String date, Time time, Theme theme) {
        long reservedCount =
            reservationRepository.countByDateAndTimeAndThemeAndStatus(date, time, theme,
                ReservationStatus.RESERVED);
        return (reservedCount < MAX_RESERVED_COUNT) ? ReservationStatus.RESERVED
            : ReservationStatus.WAITING;
    }

    private void validateDuplicateReservation(Long memberId, String date, Time time, Theme theme) {
        if (reservationRepository.existsByMemberIdAndDateAndTimeAndTheme(memberId, date, time,
            theme)) {
            throw new IllegalArgumentException("Already reserved");
        }
    }

    private Integer calculateWaitingRank(Reservation reservation) {
        List<Reservation> waitings = reservationRepository.findByDateAndTimeAndThemeAndStatusOrderByIdAsc(
            reservation.getDate(),
            reservation.getTime(),
            reservation.getTheme(),
            ReservationStatus.WAITING
        );
        return waitings.indexOf(reservation) + 1;
    }

    private void promoteWaitingToReserved(Reservation cancelledReservation) {
        reservationRepository.findFirstByDateAndTimeAndThemeAndStatus(
            cancelledReservation.getDate(),
            cancelledReservation.getTime(),
            cancelledReservation.getTheme(),
            ReservationStatus.WAITING
        ).ifPresent(Reservation::promoteToReserved);
    }
}
