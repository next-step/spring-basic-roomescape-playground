package roomescape.member;

import org.springframework.data.jpa.repository.JpaRepository;

public class MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmailAndPassword(String email, String password) {
        return null;
    }
}
