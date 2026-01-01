package roomescape.service;

import java.util.ArrayList;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import roomescape.dto.MyReservationResponse;
import roomescape.model.Waiting;
import roomescape.repository.ReservationRepository;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.exception.BadRequestException;
import roomescape.model.Member;
import roomescape.model.Reservation;
import roomescape.repository.WaitingRepository;

@Service
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final WaitingRepository waitingRepository;

    public ReservationService(ReservationRepository reservationRepository, WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.waitingRepository = waitingRepository;
    }

    public ReservationResponse create(ReservationRequest request, Member member) {
        validateRequest(request, member);

        if (reservationRepository.existsByDateAndTimeAndTheme(request.date(), request.time(), request.theme())) {
            throw new BadRequestException("이미 예약이 존재합니다.");
        }

        ReservationRequest finalized;

        if (StringUtils.hasText(request.name())) {
            finalized = new ReservationRequest(request.name(), null, request.date(), request.time(), request.theme());
        } else {
            finalized = new ReservationRequest(null, member.getId(), request.date(), request.time(), request.theme());
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

    public List<MyReservationResponse> findByMemberId(Long memberId) {
        List<MyReservationResponse> result = new ArrayList<>();

        // 예약
        reservationRepository.findByMemberId(memberId)
                .forEach(r -> result.add(
                        new MyReservationResponse(
                                r.getId(),
                                r.getTheme().getName(),
                                r.getDate(),
                                r.getTime().getValue(),
                                "예약"
                        )
                ));

        // 예약 대기
        waitingRepository.findWaitingsWithRankByMemberId(memberId)
                .forEach(wr -> {
                    Waiting w = wr.waiting();
                    result.add(
                            new MyReservationResponse(
                                    w.getId(),
                                    w.getTheme().getName(),
                                    w.getDate(),
                                    w.getTime().getValue(),
                                    wr.rank() + "번째 예약대기"
                            )
                    );
                });

        return result;
    }

    private String resolveName(ReservationRequest request, Member member) {
        if (request.name() != null) return request.name();
        if (member != null) return member.getName();

        throw new BadRequestException("예약자 이름은 누락될 수 없습니다.");
    }

    private void validateRequest(ReservationRequest request, Member member) {
        if (member == null && !StringUtils.hasText(request.name())) {
            throw new BadRequestException("예약자 정보가 없습니다.");
        }

        if (member != null &&
                waitingRepository.existsByMemberAndDateAndTimeAndTheme(member.getId(), request.date(), request.time(), request.theme())) {
            throw new BadRequestException("이미 예약 대기 중입니다.");
        }

        if (member != null &&
                reservationRepository.existsByMemberAndDateAndTimeAndTheme(member.getId(), request.date(), request.time(), request.theme())) {
            throw new BadRequestException("이미 예약했습니다.");
        }
    }
}
