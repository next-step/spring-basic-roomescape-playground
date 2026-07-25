package roomescape.global.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.entity.Member;
import roomescape.member.entity.Role;
import roomescape.member.repository.MemberRepository;

@Component
@Profile("prod")
public class DataLoader implements CommandLineRunner {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        createMember("어드민", "admil@email.com", "password", Role.ADMIN);
        createMember("브라운", "brown@email.com", "password", Role.USER);
    }

    private void createMember(String name, String email, String password, Role role) {
        if (!validateMemberExists(email)) {
            memberRepository.save(
                    new Member(name, email, password, role)
            );
        }
    }

    private boolean validateMemberExists(String email) {
        return memberRepository.findByEmail(email)
                .isPresent();
    }
}
