package roomescape.data;

import org.springframework.boot.CommandLineRunner;
import roomescape.member.MemberRepository;
import roomescape.member.model.Member;

public class DataLoader implements CommandLineRunner {

    private final MemberRepository memberRepository;

    public DataLoader(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(final String... args) throws Exception {
        System.out.println("데이터 로더 실행");

        memberRepository.save(new Member("어드민", "admin@email.com", "password", "ADMIN"));
        memberRepository.save(new Member("브라운", "brown@email.com", "password", "USER"));
    }
}