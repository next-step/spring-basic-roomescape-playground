package roomescape.reservation;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.reservation.dto.MyReservationResponse;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.waiting.Waiting;
import roomescape.reservation.waiting.WaitingRepository;
import roomescape.reservation.waiting.WaitingWithRank;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final WaitingRepository waitingRepository;

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
    public ReservationResponse saveByMember(ReservationRequest request, Member member) {
        Theme theme = findTheme(request.getTheme());
        Time time = findTime(request.getTime());

        if (waitingRepository.existsByMemberAndDateTime(member.getId(), request.getDate(), theme.getId(), time.getId())) {
            throw new IllegalArgumentException("이미 대기 신청을 한 타임입니다.");
        }

        List<Reservation> existing = reservationRepository.findByDateAndThemeId(request.getDate(), theme.getId());

        if (existing.isEmpty()) {
            Reservation reservation = new Reservation(member.getName(), request.getDate(), time, theme, member);
            reservationRepository.save(reservation);
            return ReservationResponse.from(reservation);
        }

        Waiting waiting = new Waiting(member, theme, time, request.getDate());
        waitingRepository.save(waiting);

        // 주의: 프론트엔드와 협의하여 대기 시 어떤 응답을 줄지 결정 (여기선 임시로 null이나 예외)
        return null;
    }

    public ReservationResponse saveByAdmin(ReservationRequest request) {
        Theme theme = findTheme(request.getTheme());
        Time time = findTime(request.getTime());

        Reservation reservation = new Reservation(request.getName(), request.getDate(), time, theme);
        reservationRepository.save(reservation);
        return ReservationResponse.from(reservation);
    }

    @Transactional
    public List<MyReservationResponse> findByMember(Member member) {
        List<MyReservationResponse> responses = new java.util.ArrayList<>(
                reservationRepository.findByMemberId(member.getId()).stream()
                        .map(MyReservationResponse::from)
                        .toList()
        );

        List<WaitingWithRank> waitingsWithRank = waitingRepository.findWaitingsWithRankByMemberId(member.getId());

        for (WaitingWithRank wr : waitingsWithRank) {
            Waiting w = wr.getWaiting();
            responses.add(new MyReservationResponse(
                    w.getId(),
                    w.getTheme().getName(),
                    w.getDate(),
                    w.getTime().getValue(),
                    wr.getRank() + "번째 예약대기" // 테스트 코드 assertThat(status).isEqualTo("1번째 예약대기") 대응
            ));
        }

        return responses;
    }


    private Theme findTheme(Long id) {
        return themeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("테마 없음"));
    }

    private Time findTime(Long id) {
        return timeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("시간 없음"));
    }


    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(),
                        it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }
}
