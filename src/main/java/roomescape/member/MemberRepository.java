package roomescape.member;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public Optional<Member> findByEmailAndPassword(String email, String password) {
        try {
            Member member = em.createQuery(
                            "SELECT m FROM Member m WHERE m.email = :email AND m.password = :password",
                            Member.class)
                    .setParameter("email", email)
                    .setParameter("password", password)
                    .getSingleResult();
            return Optional.of(member);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<Member> findByEmail(String email) {
        try {
            Member member = em.createQuery(
                            "SELECT m FROM Member m WHERE m.email = :email",
                            Member.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.of(member);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }
}