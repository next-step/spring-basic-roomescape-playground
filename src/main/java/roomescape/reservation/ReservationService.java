package roomescape.reservation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import roomescape.AuthenticationException;
import roomescape.ConflictException;
import roomescape.NotFoundException;
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
public class ReservationService {
    private static final Logger log = LoggerFactory.getLogger(ReservationService.class);

    private ReservationDao reservationDao;
    private MemberDao memberDao;
    private TimeDao timeDao;
    private ThemeDao themeDao;
    private final Map<String, IdempotencyRecord> idempotencyRecords = new ConcurrentHashMap<>();

    public ReservationService(ReservationDao reservationDao, MemberDao memberDao, TimeDao timeDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
        this.timeDao = timeDao;
        this.themeDao = themeDao;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, Optional<LoginMemberInfo> loginMember) {
        return save(reservationRequest, loginMember, Optional.empty());
    }

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
                    throw new ConflictException("동일한 Idempotency-Key로 다른 요청을 처리할 수 없습니다.");
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
        Reservation reservation = reservationDao.save(reservationRequest, member.name());

        return new ReservationResponse(reservation.id(), member.name(), reservation.theme().name(), reservation.date(), reservation.time().value());
    }

    private void validateReservationTarget(ReservationRequest reservationRequest) {
        if (!themeDao.existsById(reservationRequest.getTheme())) {
            throw new NotFoundException("존재하지 않는 테마입니다.");
        }
        if (!timeDao.existsById(reservationRequest.getTime())) {
            throw new NotFoundException("존재하지 않는 시간입니다.");
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
        } catch (EmptyResultDataAccessException e) {
            log.warn(
                    "Reservation member not found. name={}, loginEmail={}",
                    reservationRequest.getName(),
                    loginMember.map(LoginMemberInfo::email).orElse(null),
                    e
            );
            throw new NotFoundException("존재하지 않는 회원입니다.", e);
        }
    }

    public void deleteById(Long id) {
        if (!reservationDao.deleteById(id)) {
            throw new NotFoundException("존재하지 않는 예약입니다.");
        }
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(it -> new ReservationResponse(it.id(), it.name(), it.theme().name(), it.date(), it.time().value()))
                .toList();
    }

    public List<ReservationMineResponse> findMine(LoginMemberInfo loginMember) {
        return reservationDao.findByMemberName(loginMember.name()).stream()
                .map(it -> new ReservationMineResponse(it.id(), it.theme().name(), it.date(), it.time().value(), "예약"))
                .toList();
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
