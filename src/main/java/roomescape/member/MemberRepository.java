package roomescape.member;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<Member, Long> {

    default Member findByEmailOrThrow(String email) {
        return findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException(String.format("Member not found: %s", email)));
    }

    Optional<Member> findByEmail(String email);

    Optional<Member> findByEmailAndPassword(String email, String password);
}
