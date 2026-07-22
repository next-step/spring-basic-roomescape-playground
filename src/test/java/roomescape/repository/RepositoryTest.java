package roomescape.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import roomescape.auth.repository.RefreshTokenRepository;
import roomescape.member.entity.Member;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.entity.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.entity.Time;
import roomescape.time.repository.TimeRepository;
import roomescape.waiting.entity.Waiting;
import roomescape.waiting.repository.WaitingRepository;

import java.util.List;

@DataJpaTest(properties = {
        "spring.sql.init.mode=never"
})
@SuppressWarnings("NonAsciiCharacters")
public class RepositoryTest {

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected ReservationRepository reservationRepository;

    @Autowired
    protected TimeRepository timeRepository;

    @Autowired
    protected ThemeRepository themeRepository;

    @Autowired
    protected WaitingRepository waitingRepository;

    @Autowired
    protected RefreshTokenRepository refreshTokenRepository;

    protected void 단일_예약_저장(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    protected void 복수_예약_저장(Reservation... reservationsToSave) {
        List<Reservation> reservations = List.of(reservationsToSave);

        reservationRepository.saveAll(reservations);
    }

    protected void 단일_멤버_저장(Member member) {
        memberRepository.save(member);
    }

    protected void 복수_멤버_저장(Member... membersToSave) {
        List<Member> members = List.of(membersToSave);

        memberRepository.saveAll(members);
    }

    protected void 단일_시간_저장(Time time) {
        timeRepository.save(time);
    }

    protected void 복수_시간_저장(Time... timesToSave) {
        List<Time> times = List.of(timesToSave);

        timeRepository.saveAll(times);
    }

    protected void 단일_테마_저장(Theme theme) {
        themeRepository.save(theme);
    }

    protected void 복수_테마_저장(Theme... themesToSave) {
        List<Theme> themes = List.of(themesToSave);

        themeRepository.saveAll(themes);
    }

    protected void 복수_대기_저장(Waiting... waitingsToSave) {
        List<Waiting> waitings = List.of(waitingsToSave);

        waitingRepository.saveAll(waitings);
    }
}
