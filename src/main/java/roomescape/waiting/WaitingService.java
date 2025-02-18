package roomescape.waiting;

import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
public class WaitingService {
    private final WaitingRepository waitingRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;

    public WaitingService(WaitingRepository waitingRepository, TimeRepository timeRepository,
                          ThemeRepository themeRepository, MemberRepository memberRepository,
                          ReservationRepository reservationRepository) {
        this.waitingRepository = waitingRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
        this.reservationRepository = reservationRepository;
    }

    public WaitingResponse create(WaitingRequest waitingRequest, String memberEmail) {
        Member member = memberRepository.findByEmailOrThrow(memberEmail);
        Time time = timeRepository.findByIdOrThrow(waitingRequest.timeId());
        Theme theme = themeRepository.findByIdOrThrow(waitingRequest.themeId());

        validateReservationExistsAndNotOwnedByMember(waitingRequest.date(), time, theme, member);
        validateDuplicateWaiting(waitingRequest.date(), time, theme, member);

        Waiting waiting = new Waiting(waitingRequest.name(), waitingRequest.date(), time, theme, member);
        Waiting savedWaiting = waitingRepository.save(waiting);
        return WaitingResponse.from(savedWaiting);
    }

    public void validateDuplicateWaiting(String date, Time time, Theme theme, Member member) {
        if (hasExistingWaiting(date, time, theme, member)) {
            throw new IllegalArgumentException(
                    String.format("Waiting already exist %s, %s, %s", date, time.getValue(), theme.getName()));
        }
    }

    public void validateReservationExistsAndNotOwnedByMember(String date, Time time, Theme theme, Member member) {
        Reservation reservation = reservationRepository.findWithMemberByDateAndTimeAndTheme(date, time, theme)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Reservation is available %s, %s, %s", date, time.getValue(), theme.getName())));

        if (reservation.getMember().equals(member)) {
            throw new IllegalArgumentException(
                    String.format("Reservation already exist %s, %s, %s", date, time.getValue(), theme.getName()));
        }
    }

    public boolean hasExistingWaiting(String date, Time time, Theme theme, Member member) {
        return waitingRepository.existsByDateAndTimeAndThemeAndMember(date, time, theme, member);
    }
}
