package roomescape.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import roomescape.exception.NotFoundDataException;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmailAndPassword(String email, String password);

    Optional<Member> findByName(String name);

    default Member findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new NotFoundDataException("ID " + id + "에 해당하는 회원이 존재하지 않습니다."));
    }

    default Member findByEmailAndPasswordOrThrow(String email, String password) {
        return findByEmailAndPassword(email, password)
                .orElseThrow(() -> new NotFoundDataException("이메일 또는 비밀번호가 일치하지 않습니다."));
    }

    default Member findByNameOrThrow(String name) {
        return findByName(name)
                .orElseThrow(() -> new NotFoundDataException("이름이 '" + name + "'인 회원이 존재하지 않습니다."));
    }
}
