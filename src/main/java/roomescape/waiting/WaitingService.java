package roomescape.waiting;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final MemberRepository memberRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    public WaitingResponse save(LoginMember loginMember, WaitingRequest waitingRequest) {

        Member member = memberRepository.findById(loginMember.getId()).get();
        Time time = timeRepository.findById(waitingRequest.getTime()).get();
        Theme theme = themeRepository.findById(waitingRequest.getTheme()).get();

        Waiting waiting = waitingRepository.save(new Waiting(null, member, waitingRequest.getDate(), time, theme));

        int waitingNumber = waitingRepository.countWaitingNumber(waiting.getId(), theme.getId(), time.getId(), waiting.getDate()) + 1;

        return new WaitingResponse(waiting.getId(), theme.getName(), waitingRequest.getDate(), time.getTime_value(), waitingNumber);
    }



    public void deleteById(Long id) {
        waitingRepository.deleteById(id);
    }
}
