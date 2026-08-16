package roomescape.loader;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import roomescape.member.Member;
import roomescape.member.MemberDao;
import roomescape.member.MemberRole;

@Component
@Profile("!test")
public class DataLoader implements CommandLineRunner {
    private final MemberDao memberDao;

    public DataLoader(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public void run(String... args) {
        memberDao.save(new Member("어드민", "admin@email.com", "password", MemberRole.ADMIN));
        memberDao.save(new Member("브라운", "brown@email.com", "password", MemberRole.USER));
    }
}
