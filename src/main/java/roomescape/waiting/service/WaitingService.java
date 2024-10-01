package roomescape.waiting.service;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import roomescape.member.model.LoginMember;
import roomescape.member.model.Member;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.repository.WaitingRepository;
import roomescape.waiting.dto.WaitingRequest;
import roomescape.waiting.dto.WaitingResponse;
import roomescape.waiting.model.Waiting;

@Service
public class WaitingService {

    private final ReservationRepository reservationRepository;
    private final WaitingRepository waitingRepository;
    private final MemberRepository memberRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;

    public WaitingService(ReservationRepository reservationRepository, WaitingRepository waitingRepository,
        MemberRepository memberRepository, ThemeRepository themeRepository, TimeRepository timeRepository) {
        this.reservationRepository = reservationRepository;
        this.waitingRepository = waitingRepository;
        this.memberRepository = memberRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
    }

    @Transactional
    public WaitingResponse createWaiting(WaitingRequest waitingRequest, LoginMember loginMember) {
        Member member = memberRepository.getByName(loginMember.getName());
        Theme theme = themeRepository.getById(waitingRequest.theme());
        Time time = timeRepository.getById(waitingRequest.time());

        checkDuplicateReservation(member, theme, time, waitingRequest.date());

        Long rank = (long)(waitingRepository.findByThemeAndTimeAndDate(theme, time, waitingRequest.date()).size() + 1);
        Waiting waiting = waitingRepository.save(new Waiting(waitingRequest.date(), member, time, theme));

        return new WaitingResponse(waiting.getId(), rank);
    }

    private void checkDuplicateReservation(Member member, Theme theme, Time time, String date) {
        reservationRepository.findByMemberAndThemeAndTimeAndDate(member, theme, time, date)
            .ifPresent(r -> {
                throw new IllegalStateException("이미 해당 조건의 예약이 존재합니다.");
            });

        waitingRepository.findByMemberAndThemeAndTimeAndDate(member, theme, time, date)
            .ifPresent(r -> {
                throw new IllegalStateException("이미 해당 조건의 대기가 존재합니다.");
            });
    }

    @Transactional
    public void deleteWaiting(Long id) {
        Waiting waiting = waitingRepository.getById(id);
        waitingRepository.delete(waiting);
    }
}
