package roomescape.member;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
        return entityManager.createQuery(
                        "select m from Member m where m.email = :email and m.password = :password",
                        Member.class
                )
                .setParameter("email", email)
                .setParameter("password", password)
                .getSingleResult();
    }

    public Member findByName(String name) {
        return entityManager.createQuery(
                        "select m from Member m where m.name = :name",
                        Member.class
                )
                .setParameter("name", name)
                .getSingleResult();
    }

    public Member findByEmail(String email) {
        return entityManager.createQuery(
                        "select m from Member m where m.email = :email",
                        Member.class
                )
                .setParameter("email", email)
                .getSingleResult();
    }
}
