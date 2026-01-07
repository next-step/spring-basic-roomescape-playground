package roomescape.reservation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.ConflictException;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.WaitingRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {
    private ReservationRepository reservationRepository;
    private TimeRepository timeRepository;
    private ThemeRepository themeRepository;
    private WaitingRepository waitingRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              TimeRepository timeRepository,
                              ThemeRepository themeRepository,
                              WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.waitingRepository = waitingRepository;
    }

    @Transactional
    public ReservationResponse saveAdmin(ReservationRequest reservationRequest) {
        Time time = getTime(reservationRequest.time());
        Theme theme = getTheme(reservationRequest.theme());
        validateNotDuplicated(reservationRequest.date(), time.getId(), theme.getId());

        Reservation saved = reservationRepository.save(
                Reservation.adminReservation(reservationRequest.name(), reservationRequest.date(), time, theme)
        );

        return new ReservationResponse(saved.getId(), saved.getName(),
                saved.getTheme().getName(), saved.getDate(), saved.getTime().getTime());
    }

    @Transactional
    public ReservationResponse saveMember(ReservationRequest reservationRequest, Member member) {
        Time time = getTime(reservationRequest.time());
        Theme theme = getTheme(reservationRequest.theme());
        validateNotDuplicated(reservationRequest.date(), time.getId(), theme.getId());

        Reservation saved = reservationRepository.save(
                Reservation.memberReservation(reservationRequest.date(), time, theme, member)
        );

        return new ReservationResponse(saved.getId(), member.getName(),
                saved.getTheme().getName(), saved.getDate(), saved.getTime().getTime());
    }

    @Transactional
    public void deleteById(Long id, Long memberId) {
        reservationRepository.deleteById(id, memberId);
    }


    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getTime()))
                .toList();
    }

    public List<MyReservationResponse> findMine(Long memberId) {
        List<MyReservationResponse> reservations = reservationRepository.findByMemberId(memberId).stream()
                .map(r -> new MyReservationResponse(
                        r.getId(),
                        r.getTheme().getName(),
                        r.getDate(),
                        r.getTime().getTime(),
                        "예약"
                ))
                .toList();

        List<MyReservationResponse> waitings = waitingRepository.findWaitingsWithRankByMemberId(memberId).stream()
                .map(wr -> new MyReservationResponse(
                        wr.getWaiting().getId(),
                        wr.getWaiting().getTheme().getName(),
                        wr.getWaiting().getDate(),
                        wr.getWaiting().getTime().getTime(),
                        (wr.getRank() + 1) + "번째 예약대기"
                ))
                .toList();

        List<MyReservationResponse> result = new ArrayList<>();
        result.addAll(reservations);
        result.addAll(waitings);
        return result;
    }

    private Time getTime(Long timeId) {
        return timeRepository.findById(timeId)
                .orElseThrow(() -> new IllegalArgumentException(timeId + " 존재하지 않는 시간입니다."));
    }

    private Theme getTheme(Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException(themeId + " 존재하지 않는 테마입니다."));
    }

    private void validateNotDuplicated(String date, Long timeId, Long themeId) {
        if (reservationRepository.existsByDateTimeTheme(date, timeId, themeId)) {
            throw new ConflictException("이미 예약된 시간입니다.");
        }
    }
}
