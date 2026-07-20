package roomescape.reservation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.AuthenticationException;
import roomescape.exception.ConflictException;
import roomescape.exception.ErrorCode;
import roomescape.exception.NotFoundException;
import roomescape.auth.LoginMemberInfo;
import roomescape.member.Member;
import roomescape.member.MemberDao;
import roomescape.theme.Theme;
import roomescape.theme.ThemeDao;
import roomescape.time.Time;
import roomescape.time.TimeDao;
import roomescape.waiting.WaitingService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional(readOnly = true)
public class ReservationService {
    private static final Logger log = LoggerFactory.getLogger(ReservationService.class);

    private final ReservationDao reservationDao;
    private final MemberDao memberDao;
    private final TimeDao timeDao;
    private final ThemeDao themeDao;
    private final WaitingService waitingService;
    private final Map<String, IdempotencyRecord> idempotencyRecords = new ConcurrentHashMap<>();

    public ReservationService(ReservationDao reservationDao, MemberDao memberDao, TimeDao timeDao,
                              ThemeDao themeDao, WaitingService waitingService) {
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
        this.timeDao = timeDao;
        this.themeDao = themeDao;
        this.waitingService = waitingService;
    }

    @Transactional
    public ReservationResponse save(ReservationRequest reservationRequest, Optional<LoginMemberInfo> loginMember) {
        return save(reservationRequest, loginMember, Optional.empty());
    }

    @Transactional
    public ReservationResponse save(
            ReservationRequest reservationRequest,
            Optional<LoginMemberInfo> loginMember,
            Optional<String> idempotencyKey
    ) {
        Optional<String> key = idempotencyKey.map(String::trim).filter(it -> !it.isBlank());
        if (key.isEmpty()) {
            return createReservation(reservationRequest, loginMember);
        }

        ReservationFingerprint fingerprint = ReservationFingerprint.from(reservationRequest, loginMember);
        synchronized (idempotencyRecords) {
            IdempotencyRecord record = idempotencyRecords.get(key.get());
            if (record != null) {
                if (!record.fingerprint().equals(fingerprint)) {
                    throw new ConflictException(ErrorCode.IDEMPOTENCY_KEY_CONFLICT);
                }
                return record.response();
            }

            ReservationResponse response = createReservation(reservationRequest, loginMember);
            idempotencyRecords.put(key.get(), new IdempotencyRecord(fingerprint, response));
            return response;
        }
    }

    private ReservationResponse createReservation(ReservationRequest reservationRequest, Optional<LoginMemberInfo> loginMember) {
        Member member = findReservationMember(reservationRequest, loginMember);
        Theme theme = findTheme(reservationRequest.getTheme());
        Time time = findTime(reservationRequest.getTime());
        Reservation reservation = reservationDao.save(
                new Reservation(reservationRequest.getDate(), member, time, theme)
        );

        return toResponse(reservation);
    }

    private Theme findTheme(Long themeId) {
        return themeDao.findByIdAndDeletedFalse(themeId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.THEME_NOT_FOUND));
    }

    private Time findTime(Long timeId) {
        return timeDao.findByIdAndDeletedFalse(timeId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.TIME_NOT_FOUND));
    }

    private Member findReservationMember(ReservationRequest reservationRequest, Optional<LoginMemberInfo> loginMember) {
        Optional<Member> member = findMember(reservationRequest, loginMember);
        if (member.isEmpty()) {
            log.warn(
                    "Reservation member not found. name={}, loginEmail={}",
                    reservationRequest.getName(),
                    loginMember.map(LoginMemberInfo::email).orElse(null)
            );
            throw new NotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }
        return member.get();
    }

    private Optional<Member> findMember(ReservationRequest reservationRequest, Optional<LoginMemberInfo> loginMember) {
        if (reservationRequest.getName() != null && !reservationRequest.getName().isBlank()) {
            return memberDao.findByName(reservationRequest.getName());
        }
        if (loginMember.isEmpty()) {
            throw new AuthenticationException();
        }
        return memberDao.findByEmail(loginMember.get().email());
    }

    @Transactional
    public void deleteById(Long id) {
        if (!reservationDao.existsById(id)) {
            throw new NotFoundException(ErrorCode.RESERVATION_NOT_FOUND);
        }
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAllResponses();
    }

    public List<ReservationMineResponse> findMine(LoginMemberInfo loginMember) {
        List<ReservationMineResponse> result = new java.util.ArrayList<>(reservationDao.findByMemberId(loginMember.id()));
        result.addAll(waitingService.findMine(loginMember.id()));
        return result;
    }

    private ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.id(),
                reservation.member().name(),
                reservation.theme().name(),
                reservation.date(),
                reservation.time().value()
        );
    }

    private record ReservationFingerprint(String name, String loginEmail, String date, Long theme, Long time) {
        private static ReservationFingerprint from(ReservationRequest request, Optional<LoginMemberInfo> loginMember) {
            return new ReservationFingerprint(
                    request.getName(),
                    loginMember.map(LoginMemberInfo::email).orElse(null),
                    request.getDate(),
                    request.getTheme(),
                    request.getTime()
            );
        }
    }

    private record IdempotencyRecord(ReservationFingerprint fingerprint, ReservationResponse response) {
    }
}
