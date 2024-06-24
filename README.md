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
[x] 토큰 생성하는 객체 분리  
[x] `Closed` 된 리뷰 참고하기(https://github.com/next-step/spring-basic-roomescape-playground/pull/9/files#diff-00b248eb7ee095af211541b6c6ae4b2cd3c82b99efcedbabed5a23d3c908d6e9)

## 2단계 - 로그인 리팩터링
[x] 쿠키를 통해 회원을 조회하는 기능 리팩터링
- `ArgumentResolver`를 통해 컨트롤러에서 인증정보를 조회하는 로직을 분리.
- 구현한 `ArgumentResolver`를 활용하여 `/reservation API` 리팩토링 후 테스트

## 3단계 - 관리자 기능
[x] `HandlerInterceptor`를 구현하여, 컨트롤러의 메서드에 진입하기 전에 권한을 확인한다.
  - `preHandle`을 구현
  - 토큰의 Payload에 들어있는 `ROLE` 값을 확인하여 권환 확인

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
