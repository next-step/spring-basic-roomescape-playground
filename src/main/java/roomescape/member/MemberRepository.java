package roomescape.member;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmailAndPassword(String email, String password);

    Optional<Member> findByName(String name);

    default Member getById(Long id) {
        return findById(id)
            .orElseThrow(IllegalArgumentException::new);
    }
}
