package roomescape.reservation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.member.MemberRepository;
import roomescape.member.Member;


import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

	public ReservationService(ReservationRepository reservationRepository, TimeRepository timeRepository, ThemeRepository themeRepository, MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

	@Transactional
	public ReservationResponseDto save(ReservationRequestDto reservationRequest, Long loginMemberId) {
		Time time = timeRepository.findById(reservationRequest.time()).orElseThrow();
		Theme theme = themeRepository.findById(reservationRequest.theme()).orElseThrow();
		Reservation reservation = new Reservation(reservationRequest.name(), reservationRequest.date(), time, theme);
        if (loginMemberId != null) {
            Member memberRef = memberRepository.getReferenceById(loginMemberId);
            reservation.setMember(memberRef);
        }
        reservation = reservationRepository.save(reservation);

		return new ReservationResponseDto(reservation.getId(), reservation.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

	@Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

	public List<ReservationResponseDto> findAll() {
        return reservationRepository.findAll().stream()
				.map(it -> new ReservationResponseDto(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }


}
