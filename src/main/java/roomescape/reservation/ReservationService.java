package roomescape.reservation;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.auth.LoginMember;
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

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
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
                        "예약"
                ))
                .toList();
    }
}
