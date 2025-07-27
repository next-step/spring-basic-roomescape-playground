package roomescape.reservation;

import roomescape.auth.dto.LoginMember;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.reservation.dto.MyReservationResponse;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.WaitingRepository;
import roomescape.waiting.dto.WaitingWithRank;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final WaitingRepository waitingRepository;
    private final MemberRepository memberRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;

    public ReservationService(ReservationRepository reservationRepository, WaitingRepository waitingRepository, MemberRepository memberRepository,
                              ThemeRepository themeRepository, TimeRepository timeRepository) {
        this.reservationRepository = reservationRepository;
        this.waitingRepository = waitingRepository;
        this.memberRepository = memberRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
    }

    @Transactional
    public ReservationResponse create(ReservationRequest request, LoginMember loginMember) {
        if (request.getTheme() == null) {
            throw new IllegalArgumentException("테마 ID는 필수입니다.");
        }
        if (request.getTime() == null) {
            throw new IllegalArgumentException("시간 ID는 필수입니다.");
        }
        if (request.getDate() == null) {
            throw new IllegalArgumentException("예약 날짜는 필수입니다.");
        }

        Member loggedInUser = memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new IllegalArgumentException("로그인한 사용자 정보를 찾을 수 없습니다."));

        Member reservationHolder = determineReservationHolder(request, loggedInUser);

        Theme theme = themeRepository.findById(request.getTheme())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));

        Time time = timeRepository.findById(request.getTime())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));

        LocalDate date = LocalDate.parse(request.getDate(), DateTimeFormatter.ISO_LOCAL_DATE);

        Reservation reservationToSave = new Reservation(reservationHolder, theme, time, date);
        Reservation savedReservation = reservationRepository.save(reservationToSave);

        return ReservationResponse.from(savedReservation);
    }

    private Member determineReservationHolder(ReservationRequest request, Member loggedInUser) {
        boolean isAdminCreatingForSomeoneElse = "ADMIN".equals(loggedInUser.getRole())
                && request.getName() != null
                && !request.getName().isBlank();
        if (isAdminCreatingForSomeoneElse) {
            return memberRepository.findByName(request.getName())
                    .orElseThrow(() -> new IllegalArgumentException("예약 대상 사용자를 찾을 수 없습니다."));
        }
        return loggedInUser;
    }

    public List<MyReservationResponse> findMyReservations(Long memberId) {
        List<Reservation> reservations = reservationRepository.findWithDetailsByMemberId(memberId);
        List<WaitingWithRank> waitings = waitingRepository.findWaitingsWithRankByMemberId(memberId);

        Stream<MyReservationResponse> reservationResponses = reservations.stream()
                .map(r -> MyReservationResponse.from(r, r.getTheme(), r.getTime()));

        Stream<MyReservationResponse> waitingResponses = waitings.stream()
                .map(w -> MyReservationResponse.from(w, w.getWaiting().getTheme(), w.getWaiting().getTime()));

        return Stream.concat(reservationResponses, waitingResponses).toList();
    }

    public List<ReservationResponse> findAll() {
        List<Reservation> reservations = reservationRepository.findAllWithDetails();

        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse findResponseById(Long id) {
        Reservation reservation = reservationRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        return ReservationResponse.from(reservation);
    }

    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
}
