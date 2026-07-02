package roomescape.reservation;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.auth.LoginMember;
import roomescape.auth.UnauthorizedException;
import roomescape.member.MemberRepository;
import roomescape.theme.ThemeRepository;
import roomescape.time.TimeRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            TimeRepository timeRepository,
            ThemeRepository themeRepository,
            MemberRepository memberRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        if (reservationRequest.getName() == null || reservationRequest.getName().isBlank()) {
            if (loginMember.getName().isBlank()) {
                throw new IllegalArgumentException("예약자 이름 또는 로그인 정보가 필요합니다.");
            }

            reservationRequest.setName(loginMember.getName());
        }

        Reservation reservation = reservationRepository.save(new Reservation(
                reservationRequest.getName(),
                reservationRequest.getDate(),
                false,
                timeRepository.getReferenceById(reservationRequest.getTime()),
                themeRepository.getReferenceById(reservationRequest.getTheme()),
                memberRepository.getReferenceById(loginMember.getId())
        ));

        return new ReservationResponse(
                reservation.getId(),
                reservationRequest.getName(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue()
        );
    }

    public WaitingResponse saveWaiting(WaitingRequest waitingRequest, LoginMember loginMember) {
        if (waitingRequest.getName() == null || waitingRequest.getName().isBlank()) {
            if (loginMember.getName().isBlank()) {
                throw new IllegalArgumentException("예약자 이름 또는 로그인 정보가 필요합니다.");
            }

            waitingRequest.setName(loginMember.getName());
        }

        Reservation reservation = reservationRepository.save(new Reservation(
                waitingRequest.getName(),
                waitingRequest.getDate(),
                true,
                timeRepository.getReferenceById(waitingRequest.getTime()),
                themeRepository.getReferenceById(waitingRequest.getTheme()),
                memberRepository.getReferenceById(loginMember.getId())
        ));

        int waitingNumber = getWaitingPosition(reservation);

        return new WaitingResponse(
                reservation.getId(),
                waitingNumber,
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue()
        );
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public void deleteWaitingById(Long id, Long memberId) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("대기 예약을 찾을 수 없습니다."));

        if (!reservation.isWaiting()) {
            throw new IllegalArgumentException("대기 예약이 아닙니다.");
        }

        if (!reservation.getMember().getId().equals(memberId)) {
            throw new UnauthorizedException("본인의 대기 예약만 취소할 수 있습니다.");
        }

        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .filter(reservation -> !reservation.isWaiting())
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    public List<ReservationMineResponse> findByMemberId(Long memberId) {
        return reservationRepository.findByMember_Id(memberId).stream()
                .map(it -> new ReservationMineResponse(
                        it.getId(),
                        it.getTheme().getName(),
                        it.getDate(),
                        it.getTime().getValue(),
                        it.isWaiting() ? getWaitingPosition(it) + "번째 예약대기" : "예약"
                ))
                .toList();
    }

    private int getWaitingPosition(Reservation reservation) {
        List<Reservation> waitings = reservationRepository
                .findByDateAndTheme_IdAndIsWaitingTrueOrderByTime_TimeValueAscIdAsc(
                        reservation.getDate(),
                        reservation.getTheme().getId()
                );

        for (int i = 0; i < waitings.size(); i++) {
            if (waitings.get(i).getId().equals(reservation.getId())) {
                return i + 1;
            }
        }

        throw new IllegalArgumentException("대기 순서를 찾을 수 없습니다.");
    }
}
