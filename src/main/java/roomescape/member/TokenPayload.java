package roomescape.member;

import java.time.Instant;

public record TokenPayload(Long memberId, String tokenId, Instant expiresAt) {
}
