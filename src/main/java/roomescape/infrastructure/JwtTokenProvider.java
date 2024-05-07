package roomescape.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.member.Member;
import roomescape.member.MemberRequest;
import roomescape.member.MemberResponse;

import java.util.Date;

@Component
public class JwtTokenProvider {


    public String createToken( Member member) {
        String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E="; // 암호화를 위한 비밀키
        String accessToken = Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("email", member.getEmail())
                .claim("role",member.getRole())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact(); //jwt builder로 token 생성
        return accessToken; //시크릿키에 담아 반환
    }

}
