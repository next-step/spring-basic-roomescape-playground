package roomescape.reservation;

import java.util.List;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import roomescape.ApiException;
import roomescape.auth.AuthorizedMember;
import roomescape.member.Member;
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
            ReservationRepository reservationRepository, TimeRepository timeRepository,
            ThemeRepository themeRepository,
            MemberRepository memberRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Member member = null;
        if (reservationRequest.getMemberId() != null) {
            member = memberRepository.getReferenceById(reservationRequest.getMemberId());
        }

        Reservation newReservation = new Reservation(
                reservationRequest.getName(),
                member,
                reservationRequest.getDate(),
                timeRepository.getReferenceById(reservationRequest.getTime()),
                themeRepository.getReferenceById(reservationRequest.getTheme())
        );
        Reservation reservation = reservationRepository.save(newReservation);

        return createReservationResponse(reservation);
    }

    public void deleteById(AuthorizedMember member, Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> ApiException.status(HttpStatus.NOT_FOUND));

        if(member.role() != Member.Role.ADMIN) {
            Long memberId = reservation.getMemberId();
            if (memberId == null || memberId != member.id()) {
                throw ApiException.status(HttpStatus.FORBIDDEN);
            }
        }

        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(this::createReservationResponse)
                .toList();
    }

    public List<MyReservationResponse> findMine(Long memberId) {
        return reservationRepository.findByMember_id(memberId).stream()
                .map(r -> new MyReservationResponse(r.getId(),
                        r.getTheme().getName(),
                        r.getDate(),
                        r.getTime().getValue(),
                        "예약"
                ))
                .toList();
    }


    private ReservationResponse createReservationResponse(Reservation reservation) {
        String name = reservation.getName();
        if (name == null) {
            name = Objects.requireNonNull(reservation.getMember()).getName();
        }

        return new ReservationResponse(reservation.getId(),
                name,
                reservation.getMemberId(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue());
    }
}
