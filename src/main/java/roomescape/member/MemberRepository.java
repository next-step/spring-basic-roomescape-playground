package roomescape.member;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepository {
    private final EntityManager entityManager;

    public MemberRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Member.class, id));
    }

    public void save(Member member){
        entityManager.persist(member);
    }

    public List<Member> findAll(){
        return entityManager.createQuery(
                "SELECT m FROM Member m",
                Member.class
        ).getResultList();
    }

    public Optional<Member> findByEmailAndPassword(String email, String password){
        List<Member> members =entityManager.createQuery(
                "SELECT m FROM Member WHERE m.email= :email AND m.password =:password",
                Member.class
        ).setParameter("email",email)
                .setParameter("password",password)
                        .getResultList();

        return members.stream().findFirst();
    }



}
