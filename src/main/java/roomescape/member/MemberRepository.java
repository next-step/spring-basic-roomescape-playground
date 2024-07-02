package roomescape.member;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MemberRepository extends CrudRepository<Member, Long> {

    @Override
    public Optional<Member> findById(Long id);

    public Member findByEmailAndPassword(String email, String password);

}
