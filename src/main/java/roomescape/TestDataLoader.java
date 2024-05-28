package roomescape;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.Member;
import roomescape.reservation.Reservation;
import roomescape.theme.Theme;
import roomescape.time.Time;

@Profile("test")
@Component
@RequiredArgsConstructor
public class TestDataLoader {

    private final InitMemberService initMemberService;

    @PostConstruct
    public void init() {
        initMemberService.init();
    }

    @Component
    static class InitMemberService {
        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void init() {
            Member member1 = new Member("어드민", "admin@email.com", "password", "ADMIN");
            Member member2 = new Member("브라운", "brown@email.com", "password", "USER");
            em.persist(member1);
            em.persist(member2);

            Theme theme1 = new Theme("테마1", "테마1입니다.");
            Theme theme2 = new Theme("테마2", "테마2입니다.");
            Theme theme3 = new Theme("테마3", "테마3입니다.");
            em.persist(theme1);
            em.persist(theme2);
            em.persist(theme3);

            Time time1 = new Time("10:00");
            Time time2 = new Time("12:00");
            Time time3 = new Time("14:00");
            Time time4 = new Time("16:00");
            Time time5 = new Time("18:00");
            Time time6 = new Time("20:00");
            em.persist(time1);
            em.persist(time2);
            em.persist(time3);
            em.persist(time4);
            em.persist(time5);
            em.persist(time6);

            Reservation reservation1 = new Reservation(member1, "", "2024-03-01", time1, theme1);
            Reservation reservation2 = new Reservation(member1, "", "2024-03-01", time2, theme2);
            Reservation reservation3 = new Reservation(member1, "", "2024-03-01", time3, theme3);
            Reservation reservation4 = new Reservation("브라운", "2024-03-01", time1, theme2);
            em.persist(reservation1);
            em.persist(reservation2);
            em.persist(reservation3);
            em.persist(reservation4);
        }
    }
}
