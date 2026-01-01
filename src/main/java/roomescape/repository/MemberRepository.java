package roomescape.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import roomescape.model.Member;

@Repository
public class MemberRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Member save(Member member) {
        entityManager.persist(member);
        return member;
    }

    public Optional<Member> findByEmailAndPassword(String email, String password) {
        String jpql = "SELECT m FROM Member m WHERE m.email = :email AND m.password = :password";
        TypedQuery<Member> query = entityManager.createQuery(jpql, Member.class);
        query.setParameter("email", email);
        query.setParameter("password", password);

        List<Member> members = query.getResultList();
        return members.stream().findFirst();
    }

    public Optional<Member> findById(String id) {
        Member member = entityManager.find(Member.class, Long.parseLong(id));
        return Optional.ofNullable(member);
    }
}
