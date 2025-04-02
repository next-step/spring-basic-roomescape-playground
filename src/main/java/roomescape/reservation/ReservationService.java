package roomescape.reservation;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
public class ReservationService {

	private final ReservationRepository reservationRepository;
	private final MemberRepository memberRepository;
	private final ThemeRepository themeRepository;
	private final TimeRepository timeRepository;

	public ReservationService(ReservationRepository reservationRepository, MemberRepository memberRepository, ThemeRepository themeRepository, TimeRepository timeRepository) {
		this.reservationRepository = reservationRepository;
		this.memberRepository = memberRepository;
		this.themeRepository = themeRepository;
		this.timeRepository = timeRepository;
	}

	public ReservationResponse save(ReservationRequest request) {
		Member foundMember = memberRepository.findByName(request.getName())
			.orElseThrow(() -> new NoSuchElementException("Member not found"));
		return saveReservationWithMember(request, foundMember);
	}

	public ReservationResponse save(ReservationRequest request, LoginMember loginMember) {
		Member foundMember = memberRepository.findById(loginMember.id())
			.orElseThrow(() -> new NoSuchElementException("Member not found"));
		return saveReservationWithMember(request, foundMember);
	}

	private ReservationResponse saveReservationWithMember(ReservationRequest request, Member member) {
		Theme foundTheme = themeRepository.findById(request.getTheme())
			.orElseThrow(() -> new NoSuchElementException("Theme not found"));
		Time foundTime = timeRepository.findById(request.getTime())
			.orElseThrow(() -> new NoSuchElementException("Time not found"));
		Reservation reservation = new Reservation(member.getName(), request.getDate(), foundTime, foundTheme);
		Reservation saved = reservationRepository.save(reservation);
		return toReservationResponse(saved);
	}

	public void deleteById(Long id) {
		reservationRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public List<ReservationResponse> findAll() {
		return reservationRepository.findAllWithThemeAndTime().stream().map(this::toReservationResponse).toList();
	}

	private ReservationResponse toReservationResponse(Reservation reservation) {
		return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
	}

}
