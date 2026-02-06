package roomescape.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.member.Role;

@Component
@Profile("prod")
public class DataLoader implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        ensureMemberExists("어드민", "admin@email.com", "password", Role.ADMIN);
        ensureMemberExists("브라운", "brown@email.com", "password", Role.USER);
    }

    private void ensureMemberExists(String name, String email, String rawPassword, Role role) {
        if (memberRepository.findByEmail(email).isPresent()) {
            return;
        }
        String encoded = passwordEncoder.encode(rawPassword);
        memberRepository.save(new Member(name, email, encoded, role));
    }
}
