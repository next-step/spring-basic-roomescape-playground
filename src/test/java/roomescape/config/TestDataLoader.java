package roomescape.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import roomescape.member.entity.Member;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.entity.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.entity.Time;
import roomescape.time.repository.TimeRepository;

import java.time.LocalDate;

import static roomescape.fixture.MemberFixture.브라운_생성;
import static roomescape.fixture.MemberFixture.어드민_생성;
import static roomescape.fixture.ReservationFixture.날짜_지정_예약_생성;
import static roomescape.fixture.ThemeFixture.테마_테마1_생성;
import static roomescape.fixture.ThemeFixture.테마_테마2_생성;
import static roomescape.fixture.ThemeFixture.테마_테마3_생성;
import static roomescape.fixture.TimeFixture.시간_시간1_생성;
import static roomescape.fixture.TimeFixture.시간_시간2_생성;
import static roomescape.fixture.TimeFixture.시간_시간3_생성;
import static roomescape.fixture.TimeFixture.시간_시간4_생성;
import static roomescape.fixture.TimeFixture.시간_시간5_생성;
import static roomescape.fixture.TimeFixture.시간_시간6_생성;

@Component
@Profile("test")
@SuppressWarnings("NonAsciiCharacters")
public class TestDataLoader {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TimeRepository timeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    public void load() {
        Member admin = memberRepository.save(어드민_생성());
        Member brown = memberRepository.save(브라운_생성());

        Time time1 = timeRepository.save(시간_시간1_생성());
        Time time2 = timeRepository.save(시간_시간2_생성());
        Time time3 = timeRepository.save(시간_시간3_생성());
        timeRepository.save(시간_시간4_생성());
        timeRepository.save(시간_시간5_생성());
        timeRepository.save(시간_시간6_생성());

        Theme theme1 = themeRepository.save(테마_테마1_생성());
        Theme theme2 = themeRepository.save(테마_테마2_생성());
        Theme theme3 = themeRepository.save(테마_테마3_생성());

        LocalDate date = LocalDate.of(2024, 3, 1);
        reservationRepository.save(날짜_지정_예약_생성(admin, date, time1, theme1));
        reservationRepository.save(날짜_지정_예약_생성(admin, date, time2, theme2));
        reservationRepository.save(날짜_지정_예약_생성(admin, date, time3, theme3));
        reservationRepository.save(날짜_지정_예약_생성(brown, date, time1, theme2));
    }
}
