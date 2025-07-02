package roomescape.member;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class MemberDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public Member findById(Long id) {
        return em.find(Member.class, id);
    }

    public Member findByEmailAndPassword(String email, String password) {
        try {
            return em.createQuery(
                            "SELECT m FROM Member m WHERE m.email = :email AND m.password = :pwd",
                            Member.class)
                    .setParameter("email", email)
                    .setParameter("pwd", password)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Member findByName(String name) {
        try {
            return em.createQuery(
                            "SELECT m FROM Member m WHERE m.name = :name", Member.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Member findByEmail(String email) {
        try {
            return em.createQuery(
                            "SELECT m FROM Member m WHERE m.email = :email",
                            Member.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
