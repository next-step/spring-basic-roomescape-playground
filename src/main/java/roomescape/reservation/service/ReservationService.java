package roomescape.reservation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import roomescape.member.dto.LoginMember;
import roomescape.member.model.Member;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.dto.MyReservationResponse;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.model.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.model.Time;
import roomescape.time.repository.TimeRepository;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final MemberRepository memberRepository;

    public ReservationResponse save(ReservationRequest request, LoginMember loginMember) {
        String name = Optional.ofNullable(request.name()).orElse(loginMember.name());
        Theme theme = themeRepository.getById(request.theme());
        Time time = timeRepository.getById(request.time());
        Member member = memberRepository.getById(loginMember.id());
        reservationRepository.checkDuplication(theme, request.date(), time);
        Reservation reservation = reservationRepository.save(
            Reservation.builder()
                .name(name)
                .date(request.date())
                .theme(theme)
                .time(time)
                .member(member)
                .build()
        );
        return ReservationResponse.from(reservation);
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
            .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(),
                it.getTime().getTime()))
            .toList();
    }

    public List<MyReservationResponse> findMyReservations(LoginMember loginMember) {
        Member member = memberRepository.getById(loginMember.id());
        return reservationRepository.findAllByMember(member).stream()
            .map(MyReservationResponse::from)
            .toList();
    }
}
