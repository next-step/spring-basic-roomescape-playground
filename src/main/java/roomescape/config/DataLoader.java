package roomescape.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.Member;
import roomescape.member.MemberRepository;

@Component
@Profile("!test")
public class DataLoader implements CommandLineRunner {

    private final MemberRepository memberRepo;

    public DataLoader(MemberRepository memberRepo) {
        this.memberRepo = memberRepo;
    }

    @Override
    @Transactional
    public void run(String... args) {
        memberRepo.save(new Member("어드민", "admin@email.com", "password", "ADMIN"));
        memberRepo.save(new Member("브라운", "brown@email.com", "password", "USER"));
    }
}
