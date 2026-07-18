package roomescape.reservation;

import jakarta.persistence.NoResultException;
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
import roomescape.theme.ThemeDao;
import roomescape.time.TimeDao;

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
    private final Map<String, IdempotencyRecord> idempotencyRecords = new ConcurrentHashMap<>();

    public ReservationService(ReservationDao reservationDao, MemberDao memberDao, TimeDao timeDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
        this.timeDao = timeDao;
        this.themeDao = themeDao;
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
        validateReservationTarget(reservationRequest);
        Member member = findReservationMember(reservationRequest, loginMember);
        Reservation reservation = reservationDao.save(
                reservationRequest.getDate(),
                member,
                reservationRequest.getTime(),
                reservationRequest.getTheme()
        );

        return toResponse(reservation);
    }

    private void validateReservationTarget(ReservationRequest reservationRequest) {
        if (!themeDao.existsById(reservationRequest.getTheme())) {
            throw new NotFoundException(ErrorCode.THEME_NOT_FOUND);
        }
        if (!timeDao.existsById(reservationRequest.getTime())) {
            throw new NotFoundException(ErrorCode.TIME_NOT_FOUND);
        }
    }

    private Member findReservationMember(ReservationRequest reservationRequest, Optional<LoginMemberInfo> loginMember) {
        try {
            if (reservationRequest.getName() != null && !reservationRequest.getName().isBlank()) {
                return memberDao.findByName(reservationRequest.getName());
            }
            if (loginMember.isEmpty()) {
                throw new AuthenticationException();
            }
            return memberDao.findByEmail(loginMember.get().email());
        } catch (NoResultException e) {
            log.warn(
                    "Reservation member not found. name={}, loginEmail={}",
                    reservationRequest.getName(),
                    loginMember.map(LoginMemberInfo::email).orElse(null),
                    e
            );
            throw new NotFoundException(ErrorCode.MEMBER_NOT_FOUND, e);
        }
    }

    @Transactional
    public void deleteById(Long id) {
        if (!reservationDao.deleteById(id)) {
            throw new NotFoundException(ErrorCode.RESERVATION_NOT_FOUND);
        }
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ReservationMineResponse> findMine(LoginMemberInfo loginMember) {
        return reservationDao.findByMemberId(loginMember.id()).stream()
                .map(it -> new ReservationMineResponse(it.id(), it.theme().name(), it.date(), it.time().value(), "예약"))
                .toList();
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
