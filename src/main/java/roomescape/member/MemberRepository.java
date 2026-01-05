package roomescape.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import roomescape.exception.ErrorMessage;
import roomescape.exception.NotFoundDataException;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmailAndPassword(String email, String password);

    Optional<Member> findByName(String name);

    default Member findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new NotFoundDataException(ErrorMessage.MEMBER_NOT_FOUND_BY_ID.format(id)));
    }

    default Member findByEmailAndPasswordOrThrow(String email, String password) {
        return findByEmailAndPassword(email, password)
                .orElseThrow(() -> new NotFoundDataException(ErrorMessage.INVALID_LOGIN_CREDENTIALS.getMessage()));
    }

    default Member findByNameOrThrow(String name) {
        return findByName(name)
                .orElseThrow(() -> new NotFoundDataException(ErrorMessage.MEMBER_NOT_FOUND_BY_NAME.format(name)));
    }
}
