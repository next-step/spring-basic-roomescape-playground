package roomescape.member;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public Optional<Member> findByEmailAndPassword(String email, String password) {
        String jpql = "SELECT m FROM Member m WHERE m.email = :email AND m.password = :password";
        return em.createQuery(jpql, Member.class)
                .setParameter("email", email)
                .setParameter("password", password)
                .getResultStream()
                .findFirst();
    }
}
