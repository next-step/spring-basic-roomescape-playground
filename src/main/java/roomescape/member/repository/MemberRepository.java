package roomescape.member.repository;

import org.springframework.data.repository.Repository;

import roomescape.member.model.Member;

public interface MemberRepository extends Repository<Member, Long> {

    Member getById(Long id);

    Member save(Member member);

    Member findByEmailAndPassword(String email, String password);

    Member getByEmailAndPassword(String email, String password);

    Member findByName(String name);
}
