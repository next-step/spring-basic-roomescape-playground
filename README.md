# 방탈출 예약 관리 — Spring MVC 인증

로그인 인증, 회원 ID 기반 예약 생성, 관리자 페이지와 변경 API의 권한 검사를 구현합니다.

## 1단계 — 로그인

- 이메일과 비밀번호를 확인해 JWT를 발급하고 HttpOnly 쿠키에 저장합니다.
- 토큰을 검증해 로그인한 회원을 조회하고, 로그아웃하면 쿠키를 만료시킵니다.
- 인증 실패는 401, 권한 부족은 403, 잘못된 입력은 400으로 응답합니다.
- 예상하지 못한 서버 오류는 400으로 변환하지 않고 500으로 처리합니다.

| 메서드 | 경로 | 기능 |
| --- | --- | --- |
| POST | /login | 로그인 및 토큰 발급 |
| GET | /login/check | 로그인 회원 조회 |
| POST | /logout | 로그아웃 |

## 2단계 — 로그인 리팩터링

- CookieTokenExtractor가 쿠키에서 토큰을 추출합니다.
- LoginMemberArgumentResolver가 LoginMember를 컨트롤러에 전달합니다.
- 컨트롤러는 예약 서비스에 로그인 회원 ID만 전달합니다.
- ReservationRequest는 record로 정의합니다.
- 회원 기능은 member, 인증·인가 기능은 auth 패키지로 분리합니다.

### 예약 생성

POST /reservations 요청에는 date, theme, time과 선택적인 memberId를 전달합니다.

| 요청 조건 | 예약자 |
| --- | --- |
| memberId 생략 | 로그인한 회원 |
| 본인의 memberId 지정 | 로그인한 회원 |
| 다른 회원의 memberId 지정 | 관리자만 허용, 일반 회원은 403 |

동명이인은 이름 대신 회원 ID로 구분하고, 예약 테이블에 member_id를 저장합니다.
관리자 화면은 관리자 전용 GET /members로 회원 목록을 가져와 이름과 이메일을 표시하고,
선택한 회원의 memberId를 전송합니다.

## 3단계 — 관리자 기능

AdminInterceptor는 컨트롤러 메서드의 @AdminOnly를 확인합니다.
관리자 판단은 Member.isAdmin()에서 수행하고 LoginMember가 위임합니다.

### 관리자 전용 경로

- GET /admin, /admin/reservation, /admin/theme, /admin/time
- GET /members
- DELETE /reservations/{id}
- POST /themes, DELETE /themes/{id}
- POST /times, DELETE /times/{id}

예약 생성은 일반 회원도 이용할 수 있습니다. 테마·시간·예약 목록 조회는 공개합니다.

| 사용자 상태 | 관리자 기능 접근 |
| --- | --- |
| 관리자 | 허용 |
| 일반 회원 | 403 |
| 비로그인 또는 유효하지 않은 토큰 | 401 |

## 테스트

JDK 17을 사용합니다. 테스트 서버는 빈 포트를 자동으로 할당받습니다.

Windows PowerShell:

    .\gradlew.bat test

검증 항목:

- 로그인 및 로그인 회원 조회
- 본인 예약과 관리자 대리 예약, 동명이인 구분
- 관리자 페이지 및 API의 비로그인·일반 회원 접근 차단
- 관리자 회원 목록 조회, 예약 생성·삭제, 테마·시간 생성·삭제
- 잘못된 입력의 400 응답 및 DB 장애의 500 응답
- 쿠키에서 토큰 추출 및 토큰 누락 처리