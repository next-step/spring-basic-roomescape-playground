package roomescape.member;

import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.member.model.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmailAndPassword(String email, String password);
}