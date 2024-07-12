package roomescape.member;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    public Optional<Member> findByName(String name);

    public Member findByEmailAndPassword(String email, String password);

}
