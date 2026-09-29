package roomescape.domain.member.repository;

import org.springframework.data.repository.CrudRepository;
import roomescape.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends CrudRepository<Member, Long> {

    Optional<Member> findByNickname(String nickname);
    boolean existsByNickname(String nickname);
    boolean existsByEmail(String email);
}
