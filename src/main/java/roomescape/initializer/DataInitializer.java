package roomescape.initializer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import roomescape.auth.Role;
import roomescape.model.Member;
import roomescape.repository.MemberRepository;

@Profile({"prod", "test"})
@Configuration
public class DataInitializer implements CommandLineRunner {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        memberRepository.save(
                new Member("어드민", "admin@email.com", passwordEncoder.encode("password"), Role.ADMIN)
        );

        memberRepository.save(
                new Member("브라운", "brown@email.com", passwordEncoder.encode("password"), Role.USER)
        );
    }
}
