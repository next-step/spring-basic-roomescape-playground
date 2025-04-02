package roomescape.reservation;

import org.springframework.stereotype.Service;
import java.util.List;
import roomescape.global.exception.RoomescapeBadRequestException;
import roomescape.global.exception.RoomescapeNotFoundException;
import roomescape.member.Member;
import roomescape.reservationTime.ReservationTimeRepository;
import roomescape.theme.Theme;
import roomescape.reservationTime.ReservationTime;
import roomescape.theme.ThemeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ThemeRepository themeRepository,
                              ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationResponse create(ReservationRequest reservationRequest) {
        validatedRequest(reservationRequest);
        Theme theme = getTheme(reservationRequest);
        ReservationTime reservationTime = getReservationTime(reservationRequest);

        Reservation reservation = reservationRepository
                .save(reservationRequest.toReservation(theme, reservationTime));
        return new ReservationResponse(reservation);
    }

    public ReservationResponse saveWithMember(ReservationRequest reservationRequest,
                                              Member member) {
        validatedRequest(reservationRequest);
        Theme theme = getTheme(reservationRequest);
        ReservationTime reservationTime = getReservationTime(reservationRequest);

        Reservation reservation = reservationRepository
                .save(reservationRequest.toReservationWithMember(theme, reservationTime, member));
        return new ReservationResponse(reservation);
    }

    private void validatedRequest(ReservationRequest reservationRequest) {
        if (reservationRepository.existsByDateAndTheme_IdAndReservationTime_Id(
                reservationRequest.date(), reservationRequest.theme(), reservationRequest.time())) {
            throw new RoomescapeBadRequestException("이미 예약 된 방입니다.");
        }
    }

    private ReservationTime getReservationTime(ReservationRequest reservationRequest) {
        return reservationTimeRepository
                .findById(reservationRequest.time())
                .orElseThrow(() -> new RoomescapeNotFoundException("예약 시간을 찾을 수 없습니다."));
    }

    private Theme getTheme(ReservationRequest reservationRequest) {
        return themeRepository.findById(reservationRequest.theme())
                .orElseThrow(() -> new RoomescapeNotFoundException("테마를 찾을 수 없습니다."));
    }

    public List<MemberReservationResponse> getMemberReservations(long memberId) {
        List<Reservation> reservations = reservationRepository.findAllByMemberId(memberId);

        return reservations.stream()
                .map(reservation -> new MemberReservationResponse(reservation))
                .toList();
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAllWithReservationTime().stream()
                .map(reservation -> new ReservationResponse(reservation))
                .toList();
    }
}
