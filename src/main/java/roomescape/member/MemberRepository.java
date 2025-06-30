package roomescape.member;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    private final EntityManager em;

    public MemberRepository(EntityManager entityManager) {
        this.em = entityManager;
    }

    public Member save(Member member) {
        em.persist(member);
        em.flush();
        return member;
    }

    public Member findByEmailAndPassword(String email, String password) {
        String jpql = "SELECT m FROM Member m WHERE m.email = :email AND m.password = :password";

        try {
            return em.createQuery(jpql, Member.class)
                    .setParameter("email", email)
                    .setParameter("password", password)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    public Member findByName(String name) {
        String jpql = "SELECT m FROM Member m WHERE m.name = :name";

        try {
            return em.createQuery(jpql, Member.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    public Member findById(Long id) {
        return em.find(Member.class, id);
    }

}
