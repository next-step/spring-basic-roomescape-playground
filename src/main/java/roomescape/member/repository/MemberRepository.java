package roomescape.member.repository;

import roomescape.member.entity.Member;

import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    Optional<Member> findById(Long memberId);

    Optional<Member> findByEmailAndPassword(String email, String password);

    Optional<Member> findByName(String name);
}
