package roomescape.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import roomescape.theme.Theme;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
