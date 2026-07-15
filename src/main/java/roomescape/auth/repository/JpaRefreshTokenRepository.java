package roomescape.auth.repository;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import roomescape.auth.entity.RefreshToken;

import java.util.Optional;

@Repository
public class JpaRefreshTokenRepository implements RefreshTokenRepository {

    private final EntityManager entityManager;

    public JpaRefreshTokenRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(RefreshToken refreshToken) {
        entityManager.persist(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByMemberId(Long memberId) {
        String jpql = "SELECT rt FROM refresh_token rt WHERE rt.member.id = :memberId";

        return entityManager.createQuery(jpql, RefreshToken.class)
                .setParameter("memberId", memberId)
                .getResultStream()
                .findFirst();
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        String jpql = "SELECT rt FROM refresh_token rt WHERE rt.token = :token";

        return entityManager.createQuery(jpql, RefreshToken.class)
                .setParameter("token", token)
                .getResultStream()
                .findFirst();
    }

    @Override
    public void deleteByMemberId(Long memberId) {
        findByMemberId(memberId)
                .ifPresent(entityManager::remove);
    }

    @Override
    public void flush() {
        entityManager.flush();
    }
}
