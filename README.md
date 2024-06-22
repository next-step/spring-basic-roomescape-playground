## 1단계 - 로그인
[x] `POST /login` 구현
- Http Request Body에 이메일과 비밀번호를 담아 전달하면 이를 통해 회원 조회
- 발급한 JWT 토큰을 쿠키에 담아 반환

[x] `GET /login/check` 구현
- `HttpServletRequest`에서 `Cookie`를 꺼낸 뒤, `token` 값을 추출
- Token에서 추출한 회원의 이름을 응답 바디에 담아서 반환
  - JWT를 생성할 때, Payload의 덩어리 중 하나인 `Claim`에 `name`이라는 키를 갖는 값을 넣어두었음.
  - 따라서 토큰을 복호화하여 Payload에 담긴 `name` 값을 가져올 수 있음.
    
[x] 패키지 리팩토링 - `dto` 패키지 분리
[ ] 토큰 생성하는 객체 분리
[ ] `Closed` 된 리뷰 참고하기(https://github.com/next-step/spring-roomescape-auth/pulls?q=is%3Apr+is%3Aclosed+sort%3Acomments-desc)

## 2단계 - 로그인 리팩터링
[ ] 쿠키를 통해 회원을 조회하는 기능 리팩터링
- `ArgumentResolver`를 통해 컨트롤러에서 인증정보를 조회하는 로직을 분리.
- 



## 추후 공부할 내용
1. Cookie 생성 시, `HTTP ONLY` 옵션을 활성화하는 이유
2. 
해당 코드가 어떠한 기능을 수행하며, `Claims`가 어떠한 역할을 하는 객체인지 알아보기
```java
Long memberId = Long.valueOf(Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=".getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody().getSubject());
```
3. 
