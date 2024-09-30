package roomescape.member.repository;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import roomescape.member.model.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByName(String name);

    Member findByEmailAndPassword(String email, String password);

    default Member getByName(String name) {
        return findByName(name)
            .orElseThrow(() -> new NoSuchElementException("Member not found"));
    }

    default Member getById(Long id) {
        return findById(id)
            .orElseThrow(() -> new NoSuchElementException("Member not found"));
    }
}
