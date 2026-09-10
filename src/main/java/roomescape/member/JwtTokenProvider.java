package roomescape.member;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
    // 토큰을 암호화 하고 복호화 할 때 사용하는 비밀 키
    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    public String createToken(Member member) {
        return Jwts.builder() // JWT 조립 도구를 불러오기
                .setSubject(member.getId().toString()) // 토큰을 만들 멤버의 id를 등록
                .claim("name", member.getName()) // name, role을 토큰 내부에 Key-value 쌍으로 넣음
                .claim("role", member.getRole())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes())) // 비밀 키를 해시 알고리즘 형태로 바꿔 서명(위조 방지 목적)
                .compact(); // 위의 설정한 정보들을 긴 문자열로 변환해 완성
    }

    // 토큰을 해독해 사용자의 ID 꺼내기
    public Long getMemberId(String token) {
        return Long.valueOf(Jwts.parserBuilder() // 토큰 해석 도구 불러오기
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes())) // 이 비밀 키로 서명된 정상적인 토큰인지 검사
                .build()
                .parseClaimsJws(token) // 해독한 데이터 보따리(Claims)를 가져옴
                .getBody().getSubject()); // 위에서 setSubject로 저장했던 member.getId()문자열을 다시 꺼낸다
    }

}
