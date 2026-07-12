package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.domain.LoginMember;
import roomescape.exception.ApplicationException;
import roomescape.member.entity.Member;
import roomescape.member.exception.MemberErrorCode;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.dto.MyReservationResponse;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.entity.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.entity.Time;
import roomescape.time.repository.TimeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository, MemberRepository memberRepository, TimeRepository timeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional
    public ReservationResponse create(ReservationRequest request, LoginMember loginMember) {
        Member member = resolveMember(request, loginMember);
        LocalDate date = LocalDate.parse(request.getDate());
        Time time = timeRepository.findById(request.getTime())
                .orElseThrow();
        Theme theme = themeRepository.findById(request.getTheme())
                .orElseThrow();

        Reservation reservation = new Reservation(member, date, time, theme);
        Reservation savedReservation = reservationRepository.save(reservation);

        return new ReservationResponse(
                savedReservation.getId(),
                member.getName(),
                savedReservation.getDate().toString(),
                savedReservation.getTime().getTimeValue(),
                savedReservation.getTheme().getName()
        );
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(
                        it.getId(),
                        it.getMember().getName(),
                        it.getDate().toString(),
                        it.getTime().getTimeValue(),
                        it.getTheme().getName()
                ))
                .toList();
    }

    public List<MyReservationResponse> findReservationsByMember(LoginMember loginMember) {
        return reservationRepository.findAllByMemberId(loginMember.id())
                .stream()
                .map(reservation -> new MyReservationResponse(
                        reservation.getId(),
                        reservation.getTheme().getName(),
                        reservation.getDate().toString(),
                        reservation.getTime().getTimeValue(),
                        "예약"
                ))
                .toList();
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    private Member resolveMember(ReservationRequest reservationRequest, LoginMember loginMember) {
        if (reservationRequest.getName() != null && !reservationRequest.getName().isBlank()) {
            return memberRepository.findByName(reservationRequest.getName())
                    .orElseThrow(() -> new ApplicationException(MemberErrorCode.MEMBER_NOT_FOUND));
        }

        return memberRepository.findById(loginMember.id())
                .orElseThrow(() -> new ApplicationException(MemberErrorCode.MEMBER_NOT_FOUND));
    }
}
