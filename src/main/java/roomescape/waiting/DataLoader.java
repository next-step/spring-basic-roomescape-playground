package roomescape.waiting;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Component
@Profile("!test")
public class DataLoader implements CommandLineRunner {

    private final MemberDao memberDao;

    public DataLoader(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!memberDao.existsByEmail("admin@email.com")) {
            memberDao.save(new Member("어드민", "admin@email.com", "password", "ADMIN"));
        }
        if (!memberDao.existsByEmail("brown@email.com")) {
            memberDao.save(new Member("브라운", "brown@email.com", "password", "USER"));
        }
    }
}
