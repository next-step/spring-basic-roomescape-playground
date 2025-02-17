package roomescape.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member save(Member member);

    Member findByEmailAndPassword(String email, String password);

    @Query("SELECT m.password FROM Member m WHERE m.email = :email")
    String findPasswordByEmail(@Param("email") String email);

    Member findByName(String name);
}
