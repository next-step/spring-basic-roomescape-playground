package roomescape.dataloader;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.member.Role;

@Profile("!test")
@Component
@Transactional
public class DataLoader implements CommandLineRunner {

    private final MemberRepository memberRepository;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Member member1 = new Member("어드민", "admin@email.com", "password", Role.ADMIN);
        Member member2 = new Member("브라운", "brown@email.com", "password", Role.USER);

        memberRepository.save(member1);
        memberRepository.save(member2);
    }
}
