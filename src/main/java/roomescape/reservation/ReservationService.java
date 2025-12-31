package roomescape.reservation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.InvalidDataException;
import roomescape.exception.NotFoundDataException;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.WaitingService;
import roomescape.waiting.WaitingWithRank;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final WaitingService waitingService;

    public ReservationService(ReservationRepository reservationRepository,
            MemberRepository memberRepository,
            TimeRepository timeRepository,
            ThemeRepository themeRepository,
            WaitingService waitingService) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.waitingService = waitingService;
    }

    @Transactional
    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        Member member = determineMember(reservationRequest, loginMember);

        Time time = timeRepository.findById(reservationRequest.getTime())
                                  .orElseThrow(() -> new NotFoundDataException("해당 시간을 찾을 수 없습니다."));

        Theme theme = themeRepository.findById(reservationRequest.getTheme())
                                     .orElseThrow(() -> new NotFoundDataException("해당 테마를 찾을 수 없습니다."));

        validateDuplicateReservation(member.getId(), reservationRequest.getDate(), time.getId(), theme.getId());

        Reservation reservation = new Reservation(
                member.getName(),
                reservationRequest.getDate(),
                time,
                theme,
                member
        );

        reservationRepository.save(reservation);

        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue()
        );
    }

    private void validateDuplicateReservation(Long memberId, String date, Long timeId, Long themeId) {
        List<Reservation> reservations = reservationRepository.findByDateAndThemeId(date, themeId);

        boolean hasAnyReservation = reservations.stream()
                .anyMatch(r -> r.getTime().getId().equals(timeId));

        if (hasAnyReservation) {
            throw new InvalidDataException("해당 시간은 이미 예약이 완료되었습니다.");
        }

        boolean hasMemberReservation = reservations.stream()
                .anyMatch(r -> r.getMember() != null
                        && r.getMember().getId().equals(memberId)
                        && r.getTime().getId().equals(timeId));

        if (hasMemberReservation) {
            throw new InvalidDataException("이미 해당 시간에 예약이 존재합니다.");
        }
    }

    private Member determineMember(ReservationRequest request, LoginMember loginMember) {
        if (request.getName() != null && !request.getName().isBlank()) {
            return memberRepository.findByName(request.getName());
        }

        if (loginMember != null) {
            return memberRepository.findById(loginMember.id());
        }

        throw new InvalidDataException("예약자 정보가 필요합니다.");
    }

    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                                    .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                                    .toList();
    }

    public List<MyReservationResponse> findMyReservations(LoginMember loginMember) {
        List<MyReservationResponse> responses = new ArrayList<>();

        List<Reservation> reservations = reservationRepository.findByMemberId(loginMember.id());
        for (Reservation reservation : reservations) {
            responses.add(new MyReservationResponse(
                    reservation.getId(),
                    reservation.getTheme().getName(),
                    reservation.getDate(),
                    reservation.getTime().getValue(),
                    "예약"
            ));
        }

        List<WaitingWithRank> waitings = waitingService.findWaitingsWithRankByMemberId(loginMember.id());
        for (WaitingWithRank waitingWithRank : waitings) {
            long rank = waitingWithRank.getRank() + 1;
            responses.add(new MyReservationResponse(
                    waitingWithRank.getWaiting().getId(),
                    waitingWithRank.getWaiting().getTheme().getName(),
                    waitingWithRank.getWaiting().getDate(),
                    waitingWithRank.getWaiting().getTime().getValue(),
                    rank + "번째 예약대기"
            ));
        }

        return responses;
    }
}
