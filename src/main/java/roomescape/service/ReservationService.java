package roomescape.service;

import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import roomescape.dto.MyReservationResponse;
import roomescape.repository.ReservationRepository;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.exception.BadRequestException;
import roomescape.model.Member;
import roomescape.model.Reservation;

@Service
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public ReservationResponse create(ReservationRequest request, Member member) {
        ReservationRequest finalized;

        if (StringUtils.hasText(request.name())) {
            finalized = new ReservationRequest(request.name(), null, request.date(), request.time(), request.theme());
        } else if (member != null) {
            finalized = new ReservationRequest(null, member.getId(), request.date(), request.time(), request.theme());
        } else {
            throw new BadRequestException("예약자 정보가 없습니다.");
        }

        Reservation reservation = reservationRepository.save(finalized);

        return ReservationResponse.from(reservation);
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public List<MyReservationResponse> findByMemberId(Long id) {
        return reservationRepository.findByMemberId(id).stream()
                .map(it -> new MyReservationResponse(it.getId(), it.getTheme().getName(), it.getDate(), it.getTime().getValue(), "예약"))
                .toList();
    }

    private String resolveName(ReservationRequest request, Member member) {
        if (request.name() != null) return request.name();
        if (member != null) return member.getName();

        throw new BadRequestException("예약자 이름은 누락될 수 없습니다.");
    }
}
