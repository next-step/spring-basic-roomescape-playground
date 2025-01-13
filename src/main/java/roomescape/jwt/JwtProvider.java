package roomescape.jwt;

import roomescape.member.Member;

public interface JwtProvider {

    String generateToken(Member member);
    boolean isValidToken(String token);
//    Long extractSubject(String token);
    String extractEmail(String token);

}
