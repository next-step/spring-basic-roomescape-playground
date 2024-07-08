package roomescape;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import roomescape.member.Member;
import roomescape.member.MemberRepository;


public class DataLoader implements CommandLineRunner {

    private MemberRepository memberRepository;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(final String... args) throws Exception {
        final Member member1 = memberRepository.save(new Member("어드민", "admin@email.com", "password", "ADMIN"));
        final Member member2 = memberRepository.save(new Member("브라운", "brown@email.com", "password", "USER"));
    }
}
