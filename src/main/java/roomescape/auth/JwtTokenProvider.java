package roomescape.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import roomescape.member.Member;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenProvider {
    private static final String SECRET_KEY = "roomescape-secret-key-for-jwt-token";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public String createToken(Member member) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(member.getEmail())
                .claim("id", member.getId())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    public LoginMemberInfo parseMember(String token) {
        try {
            Map<String, Object> claims = parseClaims(token);
            Long id = Long.valueOf(claims.get("id").toString());
            String name = claims.get("name").toString();
            String email = claims.get("sub").toString();
            String role = claims.get("role").toString();
            return new LoginMemberInfo(id, name, email, role);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid token");
        }
    }

    private Map<String, Object> parseClaims(String token) throws Exception {
        String[] parts = token.split("\\.");
        if (parts.length != 3 || !isValidSignature(parts)) {
            throw new IllegalArgumentException("Invalid token");
        }

        String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
        Map<String, Object> claims = OBJECT_MAPPER.readValue(payload, new TypeReference<>() {
        });
        Number expiration = (Number) claims.get("exp");
        if (expiration.longValue() < System.currentTimeMillis() / 1000) {
            throw new IllegalArgumentException("Expired token");
        }
        return claims;
    }

    private boolean isValidSignature(String[] parts) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] signature = mac.doFinal((parts[0] + "." + parts[1]).getBytes(StandardCharsets.UTF_8));
        String encodedSignature = Base64.getUrlEncoder().withoutPadding().encodeToString(signature);
        return encodedSignature.equals(parts[2]);
    }
}
