package roomescape.member;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.NotFoundDataException;

@Repository
@Transactional(readOnly = true)
public class MemberDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Member save(Member member) {
        entityManager.persist(member);
        return member;
    }

    public Member findByEmailAndPassword(String email, String password) {
        String jpql = "SELECT m FROM Member m WHERE m.email = :email AND m.password = :password";
        TypedQuery<Member> query = entityManager.createQuery(jpql, Member.class);
        query.setParameter("email", email);
        query.setParameter("password", password);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new NotFoundDataException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }
    }

    public Member findByName(String name) {
        String jpql = "SELECT m FROM Member m WHERE m.name = :name";
        TypedQuery<Member> query = entityManager.createQuery(jpql, Member.class);
        query.setParameter("name", name);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new NotFoundDataException("이름이 '" + name + "'인 회원이 존재하지 않습니다.");
        }
    }

    public Member findById(Long id) {
        String jpql = "SELECT m FROM Member m WHERE m.id = :id";
        TypedQuery<Member> query = entityManager.createQuery(jpql, Member.class);
        query.setParameter("id", id);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new NotFoundDataException("ID " + id + "에 해당하는 회원이 존재하지 않습니다.");
        }
    }
}
