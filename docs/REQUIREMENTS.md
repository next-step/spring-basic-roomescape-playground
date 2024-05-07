# 요구사항 정리
## step1
- [x] 로그인 기능 구현
  - [x] /login에 email, password 값을 body에 포함
  - [x] 응답에 Cookie에 "token"값으로 토큰이 포함
- [x] 로그인 후 쿠키를 이용해 사용자 정보 조회
  - [x] 상단바 우측 로그인 상태를 표현해주기 위해 사용자의 정보를 조회하는 API
  - [x] Cookie를 이용하여 로그인 사용자의 정보확인
## step2
- [x] 사용자의 정보를 조회하는 로직을 리팩터링
  - [x] Cookie에 담긴 인증 정보를 이용해서 멤버 객체를 만드는 로직을 분리
- [x] 예약 생성 API 및 기능을 리팩터링
  - [x] ReservationReqeust의 name이 없는 경우 Cookie에 담긴 정보를 활용하도록 리팩터링
    - [x] ReservationReqeust에 name값이 있으면 name으로 Member를 찾고 없으면 로그인 정보를 활용해서 Member를 찾도록 수정
## step3
- [x] 어드민 페이지 진입은 admin권한이 있는 사람만 할 수 있도록 제한
- [x] HandlerInterceptor를 활용하여 권한이 없는 경우 401코드를 응답
