package roomescape.member;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Member save(Member member) {
        entityManager.persist(member);
        return member;
    }

    public Member findByEmailAndPassword(String email, String password) {
        return entityManager.createQuery(
                        "SELECT m FROM Member m WHERE m.email = :email AND m.password = :password",
                        Member.class
                )
                .setParameter("email", email)
                .setParameter("password", password)
                .getSingleResult();
    }

    public Member findByName(String name) {
        return entityManager.createQuery(
                        "SELECT m FROM Member m WHERE m.name = :name",
                        Member.class
                )
                .setParameter("name", name)
                .getSingleResult();
    }

    public Member findById(Long id) {
        return entityManager.find(Member.class, id);
    }
}
