package roomescape.waiting;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.InvalidDataException;
import roomescape.exception.NotFoundDataException;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class WaitingService {
    private final WaitingRepository waitingRepository;
    private final MemberRepository memberRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public WaitingService(WaitingRepository waitingRepository,
                          MemberRepository memberRepository,
                          TimeRepository timeRepository,
                          ThemeRepository themeRepository,
                          ReservationRepository reservationRepository) {
        this.waitingRepository = waitingRepository;
        this.memberRepository = memberRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public WaitingResponse save(WaitingRequest waitingRequest, LoginMember loginMember) {
        Member member = memberRepository.findById(loginMember.id());

        Time time = timeRepository.findById(waitingRequest.getTime())
                .orElseThrow(() -> new NotFoundDataException("해당 시간을 찾을 수 없습니다."));

        Theme theme = themeRepository.findById(waitingRequest.getTheme())
                .orElseThrow(() -> new NotFoundDataException("해당 테마를 찾을 수 없습니다."));

        validateDuplicateReservation(member.getId(), waitingRequest.getDate(), time.getId(), theme.getId());

        long count = waitingRepository.countByDateAndTimeIdAndThemeId(
                waitingRequest.getDate(),
                time.getId(),
                theme.getId()
        );
        long rank = count + 1;

        Waiting waiting = new Waiting(waitingRequest.getDate(), time, theme, member);
        waitingRepository.save(waiting);

        return new WaitingResponse(
                waiting.getId(),
                member.getName(),
                theme.getName(),
                waiting.getDate(),
                time.getValue(),
                rank + "번째 예약대기"
        );
    }

    private void validateDuplicateReservation(Long memberId, String date, Long timeId, Long themeId) {
        List<Reservation> reservations = reservationRepository.findByDateAndThemeId(date, themeId);
        boolean hasReservation = reservations.stream()
                .anyMatch(r -> r.getMember() != null
                        && r.getMember().getId().equals(memberId)
                        && r.getTime().getId().equals(timeId));

        if (hasReservation) {
            throw new InvalidDataException("이미 해당 시간에 예약이 존재합니다.");
        }

        List<Waiting> waitings = waitingRepository.findByDateAndTimeIdAndThemeId(date, timeId, themeId);
        boolean hasWaiting = waitings.stream()
                .anyMatch(w -> w.getMember().getId().equals(memberId));

        if (hasWaiting) {
            throw new InvalidDataException("이미 해당 시간에 예약 대기가 존재합니다.");
        }
    }

    @Transactional
    public void deleteById(Long id, LoginMember loginMember) {
        Waiting waiting = waitingRepository.findById(id)
                                           .orElseThrow(() -> new NotFoundDataException("해당 대기를 찾을 수 없습니다."));

        if (!waiting.getMember().getId().equals(loginMember.id())) {
            throw new InvalidDataException("본인의 대기만 취소할 수 있습니다.");
        }

        waitingRepository.deleteById(id);
    }

    public List<WaitingWithRank> findWaitingsWithRankByMemberId(Long memberId) {
        return waitingRepository.findWaitingsWithRankByMemberId(memberId);
    }
}
