package roomescape.reservation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.ConflictException;
import roomescape.exception.FailMessage;
import roomescape.exception.ForbiddenException;
import roomescape.exception.NotFoundException;
import roomescape.member.*;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.WaitingRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final WaitingRepository waitingRepository;
    private final MemberRepository memberRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              TimeRepository timeRepository,
                              ThemeRepository themeRepository,
                              WaitingRepository waitingRepository,
                              MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.waitingRepository = waitingRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public ReservationResponse saveAdmin(ReservationRequest reservationRequest) {
        Time time = getTime(reservationRequest.time());
        Theme theme = getTheme(reservationRequest.theme());
        //validateNotDuplicated(reservationRequest.date(), time.getId(), theme.getId());

        Reservation saved = reservationRepository.save(
                Reservation.adminReservation(reservationRequest.name(), reservationRequest.date(), time, theme)
        );

        return new ReservationResponse(saved.getId(), saved.getName(),
                saved.getTheme().getName(), saved.getDate(), saved.getTime().getValue());
    }

    @Transactional
    public ReservationResponse saveMember(ReservationRequest reservationRequest, Member member) {
        Time time = getTime(reservationRequest.time());
        Theme theme = getTheme(reservationRequest.theme());
        // validateNotDuplicated(reservationRequest.date(), time.getId(), theme.getId());

        Reservation saved = reservationRepository.save(
                Reservation.memberReservation(reservationRequest.date(), time, theme, member)
        );

        return new ReservationResponse(saved.getId(), member.getName(),
                saved.getTheme().getName(), saved.getDate(), saved.getTime().getValue());
    }

    @Transactional
    public ReservationResponse create(ReservationRequest req, LoginMember loginMember) {
        if (loginMember == null) {
            return saveAdmin(req);
        }

        Member member = memberRepository.findById(loginMember.id())
                .orElseThrow(() -> new NotFoundException(FailMessage.NOT_FOUND_MEMBER));
        if (loginMember.role() == Role.ADMIN && req.name() != null && !req.name().isBlank()) {
            return saveAdmin(req);
        }
        return saveMember(req, member);
    }

    @Transactional
    public void deleteById(Long id, Long memberId) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(FailMessage.NOT_FOUND_RESERVATION));
        if (reservation.getMember() == null ||
                !memberId.equals(reservation.getMember().getId())) {
            throw new ForbiddenException(FailMessage.FORBIDDEN_OWNERSHIP);
        }

        reservationRepository.delete(reservation);
    }


    public List<ReservationResponse> findAll() {
        return reservationRepository.findAllWithRelations().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    public List<MyReservationResponse> findMine(Long memberId) {
        List<MyReservationResponse> reservations = reservationRepository.findMineWithRelations(memberId).stream()

                .map(r -> new MyReservationResponse(
                        r.getId(),
                        r.getTheme().getName(),
                        r.getDate(),
                        r.getTime().getValue(),
                        "예약"
                ))
                .toList();

        List<MyReservationResponse> waitings = waitingRepository.findWaitingsWithRankByMemberId(memberId).stream()
                .map(wr -> new MyReservationResponse(
                        wr.getWaiting().getId(),
                        wr.getWaiting().getTheme().getName(),
                        wr.getWaiting().getDate(),
                        wr.getWaiting().getTime().getValue(),
                        (wr.getRank() + 1) + "번째 예약대기"
                ))
                .toList();

        List<MyReservationResponse> result = new ArrayList<>();
        result.addAll(reservations);
        result.addAll(waitings);
        return result;
    }

    private Time getTime(Long timeId) {
        return timeRepository.findById(timeId)
                .orElseThrow(() -> new NotFoundException(FailMessage.NOT_FOUND_TIME));
    }

    private Theme getTheme(Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new NotFoundException(FailMessage.NOT_FOUND_THEME));
    }

    private void validateNotDuplicated(String date, Long timeId, Long themeId) {
        if (reservationRepository.existsByDateAndTime_IdAndTheme_Id(date, timeId, themeId)) {
            throw new ConflictException(FailMessage.CONFLICT_ALREADY_RESERVED);
        }
    }
}
