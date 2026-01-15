package roomescape.service;

import java.util.ArrayList;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import roomescape.dto.MyReservationResponse;
import roomescape.model.Waiting;
import roomescape.repository.ReservationRepository;
import roomescape.repository.TimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.exception.BadRequestException;
import roomescape.model.Member;
import roomescape.model.Reservation;
import roomescape.model.Theme;
import roomescape.model.Time;
import roomescape.repository.WaitingRepository;

@Service
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final WaitingRepository waitingRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository, WaitingRepository waitingRepository,
                            TimeRepository timeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.waitingRepository = waitingRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse create(ReservationRequest request, Member member) {
        validateRequest(request, member);

        if (reservationRepository.existsByDateAndTimeAndTheme(request.date(), request.time(), request.theme())) {
            throw new BadRequestException("이미 예약이 존재합니다.");
        }

        Time time = timeRepository.findById(request.time())
                .orElseThrow(() -> new BadRequestException("시간이 존재하지 않습니다."));
        Theme theme = themeRepository.findById(request.theme())
                .orElseThrow(() -> new BadRequestException("테마가 존재하지 않습니다."));

        Reservation reservation;
        if (StringUtils.hasText(request.name())) {
            reservation = new Reservation(request.name(), request.date(), time, theme);
        } else {
            reservation = new Reservation(member, request.date(), time, theme);
        }

        Reservation saved = reservationRepository.save(reservation);
        return ReservationResponse.from(saved);
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
