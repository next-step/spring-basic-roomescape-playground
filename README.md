# 🚪 방탈출 예약 시스템

## 프로젝트 개요

방탈출 예약 관리를 위한 어드민 웹 애플리케이션입니다. 
로그인부터 예약 추가까지 REST API로 구현하며, 권한 없는 요청에 대한 예외 처리를 포함합니다.

---

## 주요 기능

### 인증 / 인가
- 이메일·비밀번호로 로그인하면 JWT를 발급해 `token` 쿠키로 내려줍니다.
- 쿠키의 토큰으로 로그인 사용자 정보를 조회합니다 (`GET /login/check`).
- 로그아웃 시 `token` 쿠키를 만료시킵니다.
- `HandlerMethodArgumentResolver`(`LoginMemberArgumentResolver`)로 컨트롤러에 로그인 사용자(`LoginMember`)를 주입합니다.

### 예외 처리
- `ErrorCode` 인터페이스로 예외 상황별 HTTP 상태 코드와 메시지를 관리합니다. (`AuthErrorCode`, `MemberErrorCode` 구현)
- 공통 비즈니스 예외 `ApplicationException`을 던지고, `@ControllerAdvice`인 `GlobalExceptionHandler`가 일괄 처리합니다.
- 현재 처리 예외: 인증되지 않은 요청 / 유효하지 않거나 만료된 토큰(401), 존재하지 않는 회원(404), 기타 예외(400).

## 프로젝트 구조
```
roomescape/
├── RoomescapeApplication.java        # 메인 진입점
├── PageController.java               # 뷰(HTML) 페이지 라우팅
│
├── auth/                             # 인증/인가 (JWT)
│   ├── AuthController.java           # 로그인/로그아웃/로그인체크 API
│   ├── AuthService.java
│   ├── AuthErrorCode.java
│   ├── JwtTokenProvider.java         # JWT 토큰 생성/검증
│   ├── JwtTokenProperty.java         # JWT 설정값 (@ConfigurationProperties)
│   ├── LoginMemberArgumentResolver.java  # @LoginMember 주입
│   ├── LoginMember.java              # 인증된 사용자 정보
│   ├── LoginRequest.java
│   └── LoginCheckResponse.java
│
├── member/                           # 회원
│   ├── Member.java                   # 도메인/엔티티
│   ├── MemberController.java
│   ├── MemberService.java
│   ├── MemberDao.java                # JdbcTemplate 기반 DAO
│   ├── MemberRequest.java
│   └── MemberResponse.java
│
├── reservation/                      # 예약 (핵심 도메인)
│   ├── Reservation.java
│   ├── ReservationController.java
│   ├── ReservationService.java
│   ├── ReservationDao.java
│   ├── ReservationRequest.java
│   └── ReservationResponse.java
│
├── theme/                            # 테마
│   ├── Theme.java
│   ├── ThemeController.java
│   └── ThemeDao.java                 # ※ Service 없음
│
├── time/                             # 예약 시간
│   ├── Time.java
│   ├── AvailableTime.java            # 예약 가능 시간 표현
│   ├── TimeController.java
│   ├── TimeService.java
│   └── TimeDao.java
│
├── config/
│   └── WebConfig.java                # ArgumentResolver 등록
│
├── exception/                        # 전역 예외 처리
│   ├── GlobalExceptionHandler.java   # @RestControllerAdvice
│   ├── ApplicationException.java
│   ├── ErrorCode.java                # 인터페이스 (auth의 AuthErrorCode가 구현)
│   └── ErrorResponse.java
│
└── util/
└── CookieUtil.java               # 쿠키 추출/생성 헬퍼

```