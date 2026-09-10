# Spring MVC 인증 1~3단계

## 요구사항

- POST "/login" 요청 시 이메일과 비밀번호로 로그인한다.
- 로그인에 성공하면 JWT를 생성하여 `token` 쿠키로 응답한다.
- GET "/login/check" 요청 시 쿠키의 토큰으로 로그인 회원 정보를 조회한다.

## 적용 사항

- JWT 생성과 해석을 담당하는 `JwtTokenProvider`를 추가했다.
- JWT의 subject에는 회원 id를 저장하고 이름과 권한을 claim으로 저장했다.
- 생성된 JWT를 `HttpOnly`가 적용된 `token` 쿠키로 응답했다.
- 로그인 확인 응답에는 로그인한 회원의 이름을 반환한다.