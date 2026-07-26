package roomescape.dataLoader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import roomescape.member.Member;
import roomescape.member.MemberRepository;


@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private MemberRepository memberRepository;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(String ...args){
        if(memberRepository.count()>0){
            return;
        }

        Member admin= Member.builder()
                        .name("어드민")
                        .email("admin@email.com")
                        .password("password")
                        .role("ADMIN").build();

        Member brown= Member.builder()
                .name("브라운")
                .email("brown@email.com")
                .password("password")
                .role("USER").build();

        memberRepository.save(admin);
        memberRepository.save(brown);
    }
}
