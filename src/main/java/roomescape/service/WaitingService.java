package roomescape.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import roomescape.dto.WaitingRequest;
import roomescape.dto.WaitingResponse;
import roomescape.exception.BadRequestException;
import roomescape.model.Member;
import roomescape.model.Waiting;
import roomescape.repository.ReservationRepository;
import roomescape.repository.WaitingRepository;

@Service
@Transactional
public class WaitingService {
    private final ReservationRepository reservationRepository;
    private final WaitingRepository waitingRepository;

    public WaitingService(ReservationRepository reservationRepository, WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.waitingRepository = waitingRepository;
    }

    public WaitingResponse create(WaitingRequest request, Member member) {
        // 이미 예약돼 있지 않으면 대기 불가
        if (!reservationRepository.existsByDateAndTimeAndTheme(request.date(), request.time(), request.theme())) {
            throw new BadRequestException("예약이 존재하지 않아 대기할 수 없습니다.");
        }

        // 이미 대기 중이면 중복 불가
        if (waitingRepository.existsByMemberAndDateAndTimeAndTheme(member.getId(), request.date(), request.time(), request.theme())) {
            throw new BadRequestException("이미 예약 대기 중입니다.");
        }

        Waiting waiting = waitingRepository.save(request, member);

        Long waitingNumber = waitingRepository.countByDateAndTimeAndTheme(request.date(), request.time(), request.theme());

        return new WaitingResponse(waiting.getId(), waitingNumber);
    }

    public void cancel(Long id, Member member) {
        Waiting waiting = waitingRepository.findById(id).orElseThrow(() -> new BadRequestException("예약 대기가 존재하지 않습니다."));

        // 본인 대기만 취소 가능
        if (!waiting.getMember().getId().equals(member.getId())) {
            throw new BadRequestException("본인의 예약 대기만 취소할 수 있습니다.");
        }

        waitingRepository.deleteById(id);
    }
}