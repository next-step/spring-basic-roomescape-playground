package roomescape.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import roomescape.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmailAndPassword(String email, String password);

    Optional<Member> findById(Long id);

    default Member getById(Long id) {
        return findById(id).orElseThrow();
    }
}
