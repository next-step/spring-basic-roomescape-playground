package roomescape.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    default Member getByEmailOrThrow(String email) {
        return findByEmail(email).orElseThrow(
                () -> new RoomEscapeException(ErrorCode.INVALID_LOGIN, "이메일이 잘못되었습니다."));
    }

}
