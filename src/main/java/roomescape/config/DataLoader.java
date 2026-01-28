package roomescape.config;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.member.Role;

@Component
@Profile("!test")
public class DataLoader implements CommandLineRunner {

    private final MemberRepository memberRepository;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(String... args) {
        if (memberRepository.count() == 0) {
            memberRepository.save(new Member("어드민", "admin@email.com", "password", Role.ADMIN));
            memberRepository.save(new Member("브라운", "brown@email.com", "password", Role.USER));
        }
    }
}


