package roomescape;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import roomescape.member.Member;
import roomescape.member.MemberRepository;

@Component
@Profile("product")
public class DataLoader implements CommandLineRunner {

	private final MemberRepository memberRepository;

	public DataLoader(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;

	}

	@Override
	public void run(String... args) {
		Member admin = new Member("어드민", "admin@email.com", "password", "ADMIN");
		Member brown = new Member("브라운", "brown@email.com", "password", "USER");
		memberRepository.save(admin);
		memberRepository.save(brown);
	}
}
