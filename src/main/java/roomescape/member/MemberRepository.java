package roomescape.member;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface MemberRepository extends CrudRepository<Member, Long> {
    Member findByName(String name);
    Member findByEmailAndPassword(String email, String password);
    boolean existsByEmailAndPassword(String email, String password);
}
