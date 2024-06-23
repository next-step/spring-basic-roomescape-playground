# Spring MVC (인증)

<h3>1단계 요구사항</h3>

- [ ] 로그인 페이지 호출 (login.html 페이지 응답)
- [ ] 로그인 요청(POST /login) 응답 API
  - email과 password를 이용해서 멤버 조회/ 조회한 멤버로 토큰 생성/Cookie를 통한 응답
- [ ] 인증 정보 조회(GET /login/check) API 
  - Cookie에서 토큰 정보 추출하여 멤버를 찾아 멤버 정보를 응답