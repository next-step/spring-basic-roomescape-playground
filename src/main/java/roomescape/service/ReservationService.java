package roomescape.service;

import org.springframework.stereotype.Service;

import java.util.List;
import roomescape.repository.ReservationRepository;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.exception.BadRequestException;
import roomescape.model.Member;
import roomescape.model.Reservation;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public ReservationResponse create(ReservationRequest request, Member member) {
        String name = resolveName(request, member);

        ReservationRequest finalized = new ReservationRequest(name, request.date(), request.theme(), request.time());

        Reservation reservation = reservationRepository.save(finalized);

        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    private String resolveName(ReservationRequest request, Member member) {
        if (request.name() != null) return request.name();
        if (member != null) return member.getName();

        throw new BadRequestException("예약자 이름은 누락될 수 없습니다.");
    }
}
