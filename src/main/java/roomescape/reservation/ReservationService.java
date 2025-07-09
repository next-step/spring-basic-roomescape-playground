package roomescape.reservation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.dto.LoginMember;
import roomescape.auth.exception.UnauthenticatedException;
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
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
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
        if (loginMember == null) {
            throw new UnauthenticatedException("로그인이 필요합니다.");
        }

        if (request.getTheme() == null) {
            throw new IllegalArgumentException("테마 ID는 필수입니다.");
        }

        if (request.getTime() == null) {
            throw new IllegalArgumentException("시간 ID는 필수입니다.");
        }

        if (request.getDate() == null) {
            throw new IllegalArgumentException("예약 날짜는 필수입니다.");
        }

        Member reservationHolder;
        Member loggedInUser = memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new IllegalArgumentException("로그인한 사용자 정보를 찾을 수 없습니다."));

        if ("ADMIN".equals(loggedInUser.getRole()) && request.getName() != null && !request.getName().isBlank()) {
            reservationHolder = memberRepository.findByName(request.getName())
                    .orElseThrow(() -> new IllegalArgumentException("예약 대상 사용자를 찾을 수 없습니다."));
        } else {
            reservationHolder = loggedInUser;
        }

        Theme theme = themeRepository.findById(request.getTheme())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));
        Time time = timeRepository.findById(request.getTime())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));
        LocalDate date = LocalDate.parse(request.getDate(), DateTimeFormatter.ISO_LOCAL_DATE);

        Reservation reservationToSave = new Reservation(reservationHolder, theme.getId(), time.getId(), date);
        Reservation savedReservation = reservationRepository.save(reservationToSave);

        return new ReservationResponse(
                savedReservation.getId(),
                reservationHolder.getName(),
                theme.getName(),
                savedReservation.getDate().toString(),
                time.getTime()
        );
    }

    public List<MyReservationResponse> findMyReservations(LoginMember loginMember) {
        if (loginMember == null) {
            throw new UnauthenticatedException("로그인이 필요합니다.");
        }
        List<Reservation> reservations = reservationRepository.findByMemberId(loginMember.getId());
        List<WaitingWithRank> waitings = waitingRepository.findWaitingsWithRankByMemberId(loginMember.getId());

        List<Long> themeIds = Stream.concat(
                reservations.stream().map(Reservation::getThemeId),
                waitings.stream().map(w -> w.getWaiting().getThemeId())
        ).distinct().toList();

        List<Long> timeIds = Stream.concat(
                reservations.stream().map(Reservation::getTimeId),
                waitings.stream().map(w -> w.getWaiting().getTimeId())
        ).distinct().toList();

        Map<Long, Theme> themeMap = themeRepository.findAllById(themeIds).stream()
                .collect(Collectors.toMap(Theme::getId, Function.identity()));
        Map<Long, Time> timeMap = timeRepository.findAllById(timeIds).stream()
                .collect(Collectors.toMap(Time::getId, Function.identity()));

        Stream<MyReservationResponse> reservationResponses = reservations.stream()
                .map(r -> MyReservationResponse.from(r, themeMap.get(r.getThemeId()), timeMap.get(r.getTimeId())));

        Stream<MyReservationResponse> waitingResponses = waitings.stream()
                .map(w -> MyReservationResponse.from(w, themeMap.get(w.getWaiting().getThemeId()), timeMap.get(w.getWaiting().getTimeId())));

        return Stream.concat(reservationResponses, waitingResponses).toList();
    }

    public List<ReservationResponse> findAll() {
        List<Reservation> reservations = reservationRepository.findAll();
        List<Long> memberIds = reservations.stream().map(r -> r.getMember().getId()).distinct().toList();
        List<Long> themeIds = reservations.stream().map(Reservation::getThemeId).distinct().toList();
        List<Long> timeIds = reservations.stream().map(Reservation::getTimeId).distinct().toList();

        Map<Long, Member> memberMap = memberRepository.findAllById(memberIds).stream()
                .collect(Collectors.toMap(Member::getId, Function.identity()));
        Map<Long, Theme> themeMap = themeRepository.findAllById(themeIds).stream()
                .collect(Collectors.toMap(Theme::getId, Function.identity()));
        Map<Long, Time> timeMap = timeRepository.findAllById(timeIds).stream()
                .collect(Collectors.toMap(Time::getId, Function.identity()));

        return reservations.stream()
                .map(r -> new ReservationResponse(
                        r.getId(),
                        memberMap.get(r.getMember().getId()).getName(),
                        themeMap.get(r.getThemeId()).getName(),
                        r.getDate().toString(),
                        timeMap.get(r.getTimeId()).getTime()
                ))
                .toList();
    }

    public ReservationResponse findResponseById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        Member member = reservation.getMember();
        Theme theme = themeRepository.findById(reservation.getThemeId())
                .orElseThrow(() -> new IllegalStateException("예약에 연결된 테마가 없습니다."));
        Time time = timeRepository.findById(reservation.getTimeId())
                .orElseThrow(() -> new IllegalStateException("예약에 연결된 시간이 없습니다."));

        return new ReservationResponse(
                reservation.getId(),
                member.getName(),
                theme.getName(),
                reservation.getDate().toString(),
                time.getTime()
        );
    }

    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
}
