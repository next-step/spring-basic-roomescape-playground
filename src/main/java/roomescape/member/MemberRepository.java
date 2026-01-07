package roomescape.member;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepository {
    @PersistenceContext
    private EntityManager em;

    public Member save(Member member) {
        if (member.getId() == null) {
            em.persist(member);
            return member;
        }
        return em.merge(member);
    }

    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(em.find(Member.class, id));
    }

    public List<Member> findAll() {
        return em.createQuery("select t from Member t", Member.class)
                .getResultList();
    }

    public Optional<Member> findByEmail(String email) {
        return em.createQuery(
                        "select t from Member t where t.email = :email",
                        Member.class
                )
                .setParameter("email", email)
                .getResultList()
                .stream().findFirst();
    }
}
