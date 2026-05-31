package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.WaitingRepository;
import roomescape.reservation.domain.Waiting;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.Time;
import roomescape.time.repository.TimeRepository;

@DataJpaTest
public class JpaTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TimeRepository timeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private WaitingRepository waitingRepository;

    @DisplayName("Time 레포지토리 테스트")
    @Test
    void save_and_find_time() {
        // given
        Time time = new Time("10:00");

        // when
        entityManager.persist(time);
        entityManager.flush();
        Time persistTime = timeRepository.findById(time.getId()).orElse(null);

        // then
        assertThat(persistTime.getTime()).isEqualTo(time.getTime());
    }

    @DisplayName("Theme 레포지토리 테스트")
    @Test
    void save_and_find_theme() {
        // given
        Theme theme = new Theme("Theme", "This is a Theme.");

        // when
        entityManager.persist(theme);
        entityManager.flush();
        Theme persistTheme = themeRepository.findById(theme.getId()).orElseThrow();

        // then
        assertThat(persistTheme.getName()).isEqualTo(theme.getName());
    }

    @DisplayName("Member 레포지토리 테스트")
    @Test
    void save_and_find_member() {
        // given
        Member member = new Member("tester", "test@test.com", "password", Role.USER);

        // when
        entityManager.persist(member);
        entityManager.flush();
        Member persistMember = memberRepository.findById(member.getId()).orElseThrow();

        // then
        assertThat(persistMember.getName()).isEqualTo(member.getName());
    }

    @DisplayName("Member 이름으로 조회 테스트")
    @Test
    void find_member_by_name() {
        // given
        Member member = new Member("tester", "test@test.com", "password", Role.USER);

        // when
        entityManager.persist(member);
        entityManager.flush();
        Member persistMember = memberRepository.findByName(member.getName()).orElseThrow();

        // then
        assertThat(persistMember.getEmail()).isEqualTo(member.getEmail());
    }

    @DisplayName("Member 이메일/비밀번호로 조회 테스트")
    @Test
    void find_member_by_email_and_password() {
        // given
        Member member = new Member("tester", "test@test.com", "password", Role.USER);

        // when
        entityManager.persist(member);
        entityManager.flush();
        Member persistMember = memberRepository.findByEmailAndPassword(member.getEmail(), member.getPassword()).orElseThrow();

        // then
        assertThat(persistMember.getName()).isEqualTo(member.getName());
    }

    @DisplayName("Reservation 레포지토리 테스트")
    @Test
    void save_and_find_reservation() {
        // given
        Time time = new Time("10:00");
        Theme theme = new Theme("Theme", "This is a Theme.");
        Member member = new Member("tester", "test@test.com", "password", Role.USER);
        entityManager.persist(time);
        entityManager.persist(theme);
        entityManager.persist(member);
        Reservation reservation = new Reservation("tester", "2025-01-01", time, theme, member);

        // when
        entityManager.persist(reservation);
        entityManager.flush();
        Reservation persistReservation = reservationRepository.findById(reservation.getId()).orElseThrow();

        // then
        assertThat(persistReservation.getName()).isEqualTo(reservation.getName());
        assertThat(persistReservation.getMember().getId()).isEqualTo(member.getId());
    }

    @DisplayName("Reservation 날짜/테마로 조회 테스트")
    @Test
    void find_reservations_by_date_and_theme_id() {
        // given
        Time time = new Time("10:00");
        Theme theme = new Theme("Theme", "This is a Theme.");
        Member member = new Member("tester", "test@test.com", "password", Role.USER);
        entityManager.persist(time);
        entityManager.persist(theme);
        entityManager.persist(member);
        Reservation reservation = new Reservation("tester", "2025-01-01", time, theme, member);

        // when
        entityManager.persist(reservation);
        entityManager.flush();
        List<Reservation> reservations = reservationRepository.findByDateAndThemeId("2025-01-01", theme.getId());

        // then
        assertThat(reservations).hasSize(1);
        assertThat(reservations.get(0).getDate()).isEqualTo(reservation.getDate());
    }

    @DisplayName("Reservation 멤버로 조회 테스트")
    @Test
    void find_reservations_by_member_id() {
        // given
        Time time = new Time("10:00");
        Theme theme = new Theme("Theme", "This is a Theme.");
        Member member = new Member("tester", "test@test.com", "password", Role.USER);
        entityManager.persist(time);
        entityManager.persist(theme);
        entityManager.persist(member);
        entityManager.persist(new Reservation("tester", "2025-01-01", time, theme, member));
        entityManager.persist(new Reservation("tester", "2025-01-02", time, theme, member));
        entityManager.flush();

        // when
        List<Reservation> reservations = reservationRepository.findByMemberId(member.getId());

        // then
        assertThat(reservations).hasSize(2);
    }

    @DisplayName("Waiting 멤버로 조회 테스트")
    @Test
    void find_waitings_by_member_id() {
        // given
        Time time = new Time("10:00");
        Theme theme = new Theme("Theme", "This is a Theme.");
        Member member = new Member("tester", "test@test.com", "password", Role.USER);
        entityManager.persist(time);
        entityManager.persist(theme);
        entityManager.persist(member);
        entityManager.persist(new Waiting("2025-01-01", time, theme, member));
        entityManager.flush();

        // when
        List<Waiting> waitings = waitingRepository.findByMemberId(member.getId());

        // then
        assertThat(waitings).hasSize(1);
        assertThat(waitings.get(0).getMember().getId()).isEqualTo(member.getId());
    }

}
