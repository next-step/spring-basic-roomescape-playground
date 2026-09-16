package roomescape.member.auth;

import java.time.Instant;

public record TokenPayload(Long memberId, String tokenId, Instant expiresAt) {
}
