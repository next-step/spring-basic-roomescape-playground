package roomescape.auth.repository;

import roomescape.auth.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {

    void save(RefreshToken refreshToken);

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByMemberId(Long memberId);

    void deleteByMemberId(Long memberId);

    void flush();
}
