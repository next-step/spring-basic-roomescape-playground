package roomescape;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.Member;

@Profile("production")
@Component
@RequiredArgsConstructor
public class DataLoader {

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
            Member admin = new Member("어드민", "admin@email.com", "password", "ADMIN");
            Member brown = new Member("브라운", "brown@email.com", "password", "USER");
            em.persist(admin);
            em.persist(brown);
        }
    }
}
