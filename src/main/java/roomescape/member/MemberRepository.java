package roomescape.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m where m.email = :email and m.password = :password")
    Member findByEmailAndPassword(String email, String password);

    @Query("select m from Member m where m.name=:name")
    Member findByName(String name);

    @Query("select m from Member m where m.id=:id")
    Member findMemberById(Long id);
}
