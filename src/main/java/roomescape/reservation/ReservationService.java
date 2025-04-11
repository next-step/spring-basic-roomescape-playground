package roomescape.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import roomescape.global.exception.RoomescapeBadRequestException;
import roomescape.global.exception.RoomescapeNotFoundException;
import roomescape.global.exception.RoomescapeServerError;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.reservationTime.ReservationTimeRepository;
import roomescape.theme.Theme;
import roomescape.reservationTime.ReservationTime;
import roomescape.theme.ThemeRepository;
import roomescape.waiting.WaitingRankingResponse;
import roomescape.waiting.WaitingService;

@Service
public class ReservationService {

    private final WaitingService waitingService;
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(WaitingService waitingService, ReservationRepository reservationRepository,
                              ThemeRepository themeRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              MemberRepository memberRepository) {
        this.waitingService = waitingService;
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public ReservationResponse create(ReservationRequest reservationRequest, Member member) {
        validatedRequest(reservationRequest);
        Theme theme = getTheme(reservationRequest.theme());
        ReservationTime reservationTime = getReservationTime(reservationRequest.time());

        if (member.isAdmin()) {
            Member adminMember = memberRepository.findByName("임시사용자")
                    .orElseThrow(() -> new RoomescapeServerError());
            Reservation reservation = reservationRepository
                    .save(reservationRequest.toReservationWithMember(theme, reservationTime,
                            adminMember));
            return new ReservationResponse(reservation);
        }

        Reservation reservation = reservationRepository
                .save(reservationRequest.toReservationWithMember(theme, reservationTime, member));
        return new ReservationResponse(reservation);
    }

    private void validatedRequest(ReservationRequest reservationRequest) {
        if (reservationRepository.existsByDateAndTheme_IdAndReservationTime_Id(
                reservationRequest.date(), reservationRequest.theme(), reservationRequest.time())) {
            throw new RoomescapeBadRequestException("이미 예약 된 방입니다.");
        }
        ReservationTime time = reservationTimeRepository.findById(reservationRequest.time())
                .orElseThrow(() -> new RoomescapeBadRequestException("해당 시간이 존재하지 않습니다."));
        validatedTime(reservationRequest.date(), time);
    }

    private void validatedTime(LocalDate date, ReservationTime time) {
        if (date.isAfter(LocalDate.now())) {
            return;
        }
        if (time.isBefore(LocalTime.now())) {
            throw new RoomescapeBadRequestException("현재 시각보다 이전 시간에 예약할 수 없습니다.");
        }
    }

    private ReservationTime getReservationTime(long timeId) {
        return reservationTimeRepository
                .findById(timeId)
                .orElseThrow(() -> new RoomescapeNotFoundException("예약 시간을 찾을 수 없습니다."));
    }

    private Theme getTheme(long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new RoomescapeNotFoundException("테마를 찾을 수 없습니다."));
    }

    public MemberReservationResponses getMemberReservationsAndWaitings(long memberId) {
        MemberReservationResponses results = new MemberReservationResponses(getMemberReservations(memberId));
        List<WaitingRankingResponse> memberWaitings = waitingService.getMemberWaitings(memberId);

        return results.addWaitings(memberWaitings);
    }

    public List<MemberReservationResponse> getMemberReservations(long memberId) {
        List<Reservation> reservations = reservationRepository.findAllByMemberId(memberId);

        return reservations.stream()
                .map(MemberReservationResponse::new)
                .toList();
    }

    public void deleteById(long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAllWithReservationTime().stream()
                .map(reservation -> new ReservationResponse(reservation))
                .toList();
    }
}
