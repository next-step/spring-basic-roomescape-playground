package roomescape;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import roomescape.member.Member;
import roomescape.member.MemberRepository;

@Profile("default")
@Component
public class DataLoader implements CommandLineRunner {

    private MemberRepository memberRepository;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Member admin = memberRepository.save(new Member("어드민", "admin@email.com", "password", "ADMIN"));
        Member testUser1 = memberRepository.save(new Member("브라운", "brown@email.com", "password", "USER"));
    }
}
