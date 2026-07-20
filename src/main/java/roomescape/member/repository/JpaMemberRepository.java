package roomescape.member.repository;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import roomescape.member.entity.Member;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaMemberRepository implements MemberRepository {

    private final EntityManager entityManager;

    public JpaMemberRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Member save(Member member) {
        entityManager.persist(member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        Member member = entityManager.find(Member.class, memberId);

        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByEmailAndPassword(String email, String password) {
        String jpql = "SELECT m FROM member AS m WHERE m.email = :email AND m.password = :password";
        List<Member> members = entityManager.createQuery(jpql, Member.class)
                .setParameter("email", email)
                .setParameter("password", password)
                .getResultList();

        return members.stream().findFirst();
    }

    @Override
    public Optional<Member> findByName(String name) {
        String jpql = "SELECT m FROM member AS m WHERE m.name = :name";
        List<Member> members = entityManager.createQuery(jpql, Member.class)
                .setParameter("name", name)
                .getResultList();

        return members.stream().findFirst();
    }
}
