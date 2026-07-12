package roomescape.auth.repository;

import roomescape.auth.entity.RefreshTokenEntity;

import java.util.Optional;

public interface RefreshTokenRepository {

    void save(RefreshTokenEntity refreshTokenEntity);

    Optional<RefreshTokenEntity> findByToken(String token);

    Optional<RefreshTokenEntity> findByMemberId(Long memberId);

    void deleteByMemberId(Long memberId);
}
