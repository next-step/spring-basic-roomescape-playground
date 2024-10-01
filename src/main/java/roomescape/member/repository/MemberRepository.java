package roomescape.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import roomescape.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmailAndPassword(String email, String password);
}
