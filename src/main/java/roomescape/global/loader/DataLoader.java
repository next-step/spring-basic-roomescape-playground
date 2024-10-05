package roomescape.global.loader;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import roomescape.member.Member;
import roomescape.member.MemberRepository;

@Component
public class DataLoader implements CommandLineRunner {

    private final MemberRepository memberRepository;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        memberRepository.save(new Member("어드민", "admin@email.com", "password", "ADMIN"));
        memberRepository.save(new Member("브라운", "brown@email.com", "password", "USER"));
    }
}
