# 1단계 - 로그인

## 구현 내용

JWT 기반 토큰 인증을 이용한 로그인 기능을 구현했습니다.

## 추가/변경된 파일

- `auth/LoginRequest.java` — 로그인 요청 DTO
- `auth/TokenService.java` — JWT 토큰 생성 및 파싱
- `auth/LoginController.java` — 로그인 및 인증 확인 API
- `member/MemberDao.java` — `findById` 메서드 추가
- `application.properties` — JWT secret 활성화

## API

| Method | URL | 설명 |
|--------|-----|------|
| POST | `/login` | 이메일/패스워드로 로그인, 토큰을 쿠키에 발급 |
| GET | `/login/check` | 쿠키의 토큰으로 로그인 사용자 정보 조회 |

## 인증 흐름

1. `POST /login` 요청 시 이메일/패스워드로 멤버 조회
2. JWT 토큰 생성 후 `HttpOnly` 쿠키로 응답
3. `GET /login/check` 요청 시 쿠키에서 토큰 추출 → 멤버 정보 반환

# 2단계 - 로그인 리팩터링

## 구현 내용

`HandlerMethodArgumentResolver`를 활용해 쿠키 기반 멤버 추출 로직을 분리하고,
예약 생성 시 로그인 사용자 정보를 활용하도록 리팩터링했습니다.

## 추가/변경된 파일

- `auth/LoginMember.java` — 로그인 멤버 객체
- `auth/LoginMemberArgumentResolver.java` — 쿠키 토큰으로 LoginMember 주입
- `auth/WebMvcConfiguration.java` — ArgumentResolver 등록
- `auth/LoginController.java` — `checkLogin`에 LoginMember 주입 적용
- `reservation/ReservationController.java` — 예약 생성에 LoginMember 주입
- `reservation/ReservationService.java` — name 없으면 로그인 멤버 이름 사용
- `reservation/ReservationDao.java` — `save` 메서드 name 파라미터 분리

## API 변경

| Method | URL | 변경 내용 |
|--------|-----|----------|
| GET | `/login/check` | `HttpServletRequest` 직접 파싱 → `LoginMember` 주입으로 교체 |
| POST | `/reservations` | `name` 없으면 로그인 멤버 이름으로 예약 생성 |

## 예약 생성 로직

| 요청 조건 | 사용되는 이름 |
|----------|-------------|
| `name` 있음 (관리자) | 요청의 `name`으로 멤버 조회 |
| `name` 없음 (일반 사용자) | 로그인 멤버 이름 사용 |

## 리팩터링 포인트

- 쿠키 파싱 로직이 컨트롤러마다 중복되는 문제 → `LoginMemberArgumentResolver`로 한 곳에서 처리
- 컨트롤러는 `LoginMember` 객체만 받아 사용, 인증 세부 로직 불필요

# 3단계 - 관리자 기능

## 구현 내용

`HandlerInterceptor`를 활용해 `/admin/**` 경로에 대한 권한 체크를 구현했습니다.
ADMIN 권한이 없는 사용자가 어드민 페이지에 접근하면 401을 응답합니다.

## 추가/변경된 파일

- `auth/AdminInterceptor.java` — 쿠키 토큰으로 ADMIN 권한 체크
- `auth/WebMvcConfiguration.java` — AdminInterceptor 등록 (`/admin/**`)

## 권한 체크 흐름

1. 요청에서 쿠키의 token 추출
2. JWT 파싱 → memberId 조회
3. DB에서 멤버 role 확인
4. ADMIN이 아니면 401 응답 후 컨트롤러 진입 차단

## API 변경

| 경로 | 권한 |
|------|------|
| `/admin/**` | ADMIN만 접근 가능 |
