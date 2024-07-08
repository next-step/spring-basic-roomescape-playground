package roomescape.member;

import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.member.model.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmailAndPassword(String email, String password);
    
    Member findByTime(String time);

    Member findByName(String name);

    Optional<Object> findByEmail(String adminEmail);
}