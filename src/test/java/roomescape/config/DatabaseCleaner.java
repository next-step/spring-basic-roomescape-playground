package roomescape.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import roomescape.auth.repository.RefreshTokenRepository;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.repository.TimeRepository;
import roomescape.waiting.repository.WaitingRepository;

@Component
@SuppressWarnings("NonAsciiCharacters")
@Profile("test")
public class DatabaseCleaner {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TimeRepository timeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private WaitingRepository waitingRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private TestDataLoader testDataLoader;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void clear() {
        refreshTokenRepository.deleteAll();
        waitingRepository.deleteAll();
        reservationRepository.deleteAll();
        themeRepository.deleteAll();
        timeRepository.deleteAll();
        memberRepository.deleteAll();

        resetIdentity();
        testDataLoader.load();
    }

    private void resetIdentity() {
        jdbcTemplate.execute("ALTER TABLE refresh_token ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE waiting ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE member ALTER COLUMN id RESTART WITH 1");
    }
}
