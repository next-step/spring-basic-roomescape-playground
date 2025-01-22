package roomescape;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import roomescape.member.Member;
import roomescape.member.MemberRepository;

@Profile("default")
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final MemberRepository memberRepository;

    @Override
    public void run(String... args) throws Exception {
        Member member1 = new Member("어드민", "admin@email.com", "password", "ADMIN");
        Member member2 = new Member("브라운", "brown@email.com", "password", "USER");

        memberRepository.save(member1);
        memberRepository.save(member2);
    }
}
