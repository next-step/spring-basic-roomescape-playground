package roomescape.member;

import org.springframework.data.repository.Repository;

public interface MemberRepository extends Repository<Member, Long> {

    Member save(Member member);

    Member findByEmailAndPassword(String email, String password);

    Member getByEmailAndPassword(String email, String password);

    Member findByName(String name);
}
