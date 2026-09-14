package roomescape.domain.member.repository;

import roomescape.domain.member.entity.Member;

public interface MemberRepository {

    Member save(Member member);

    boolean existsByEmail(String email);
}
