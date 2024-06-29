# Spring MVC (인증)

<h3>1단계 로그인 요구사항</h3>

- [x] 로그인 페이지 호출 (login.html 페이지 응답)
- [x] 로그인 요청(POST /login) 응답 API
  - email과 password를 이용해서 멤버 조회/ 조회한 멤버로 토큰 생성/Cookie를 통한 응답
- [x] 인증 정보 조회(GET /login/check) API 
  - Cookie에서 토큰 정보 추출하여 멤버를 찾아 멤버 정보를 응답

<h3>2단계 로그인 리팩터링 요구사항</h3>

- [x] 멤버 객체를 만드는 로직을 분리(Cookie 인증 정보 활용)
- [x] 예약 생성 시 name이 없는 경우
  - 예약 생성 시 ReservationReqeust의 name이 없는 경우 Cookie에 담긴 정보를 활용 
- [x] Find Member 수정
  - ReservationReqeust에 name값이 있으면 name으로 Member를 찾고 없으면 로그인 정보를 활용해서 Member를 찾는다.
- [x] HandlerMethodArgumentResolver 사용
- [x] 예약 생성 
  관리자는 name 인자로 전달한 정보로 예약을 생성하게 합니다. (기존 기능)
  로그인 사용자는 자신의 로그인 정보로 예약을 생성하게 합니다. (신규 기능)

<h3>3단계 관리자 기능</h3>

- [ ] 어드민 페이지 
  - admin권한이 있는 사람만 할 수 있도록 제한 
- [ ] HandlerInterceptor 
  - HandlerInterceptor를 활용하여 권한이 없는 경우 401코드 응답