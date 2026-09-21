package roomescape.reservation.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import roomescape.auth.LoginMember;
import roomescape.exception.*;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.Time;
import roomescape.time.repository.TimeRepository;

import java.util.List;

@Service
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

    public ReservationResponse save(LoginMember loginMember, ReservationRequest reservationRequest) {
        String reservationName = loginMember.name();
        String requestedName = reservationRequest.getName();

        if (requestedName != null) {
            if (requestedName.isBlank()) {
                throw new InvalidReservationException("예약자 이름을 올바르게 입력해야 합니다.");
            }

            reservationName = memberRepository.findByName(requestedName)
                    .orElseThrow(() -> new NotFoundMemberException("예약할 회원을 찾을 수 없습니다."))
                    .getName();
        }

        Time time = timeRepository.findByIdAndDeletedFalse(reservationRequest.getTime())
                .orElseThrow(() -> new NotFoundTimeException("예약 시간을 찾을 수 없습니다."));

        Theme theme = themeRepository.findByIdAndDeletedFalse(reservationRequest.getTheme())
                .orElseThrow(() -> new NotFoundThemeException("예약 테마를 찾을 수 없습니다."));

        Reservation newReservation = Reservation.create(
                reservationName,
                reservationRequest.getDate(),
                time,
                theme
        );

        Reservation savedReservation;

        try {
            savedReservation = reservationRepository.save(newReservation);
        } catch (DataIntegrityViolationException exception) {
            throw new DuplicateReservationException("이미 해당 날짜와 시간에 예약된 테마입니다.");
        }

        return new ReservationResponse(savedReservation.getId(), savedReservation.getName(), savedReservation.getTheme().getName(), savedReservation.getDate(), savedReservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundReservationException("삭제할 예약을 찾을 수 없습니다."));

        reservationRepository.delete(reservation);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }
}
