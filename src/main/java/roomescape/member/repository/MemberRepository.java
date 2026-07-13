package roomescape.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.member.model.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmailAndPassword(String email, String password);
    Optional<Member> findByName(String name);
}
