package roomescape.member;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Member save(Member member) {
        entityManager.persist(member);
        return member;
    }

    public Member getByEmailAndPassword(String email, String password) {
        return entityManager.createQuery(
                        "SELECT m FROM Member m WHERE m.email = :email AND m.password = :password", Member.class)
                .setParameter("email", email)
                .setParameter("password", password)
                .getSingleResult();
    }

    public Optional<Member> findById(Long memberId) {
        Member member = entityManager.find(Member.class, memberId);
        return Optional.ofNullable(member);
    }

    public Optional<Member> findByEmail(String email) {
        return entityManager.createQuery(
                        "SELECT m FROM Member m WHERE m.email =:email", Member.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }
}
