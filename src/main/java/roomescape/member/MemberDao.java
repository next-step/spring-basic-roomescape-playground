package roomescape.member;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {
    @PersistenceContext
    private EntityManager entityManager;

    public Member save(Member member) {
        entityManager.persist(member);
        return member;
    }

    public Member findByEmailAndPassword(String email, String password) {
        try {
            return entityManager.createQuery(
                            "select m from Member m where m.email = :email and m.password = :password",
                            Member.class
                    )
                    .setParameter("email", email)
                    .setParameter("password", password)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new EmptyResultDataAccessException(1);
        }
    }

    public Member findByName(String name) {
        try {
            return entityManager.createQuery(
                            "select m from Member m where m.name = :name",
                            Member.class
                    )
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new EmptyResultDataAccessException(1);
        }
    }

    public Member findByEmail(String email) {
        try {
            return entityManager.createQuery(
                            "select m from Member m where m.email = :email",
                            Member.class
                    )
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new EmptyResultDataAccessException(1);
        }
    }
}
