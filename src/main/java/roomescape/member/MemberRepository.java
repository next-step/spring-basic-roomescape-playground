package roomescape.member;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Member save(Member member) {
        entityManager.persist(member);
        return member;
    }

    public Optional<Member> findById(Long id) {
        Member member = entityManager.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public Optional<Member> findByEmail(String email) {
        try {
            Member member = entityManager.createQuery("SELECT m FROM Member m WHERE m.email = :email", Member.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.of(member);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public List<Member> findAll() {
        return entityManager.createQuery("SELECT m FROM Member m", Member.class)
                .getResultList();
    }
}
