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
- '/admin/**'은 ADMIN 권한을 가진 사용자만 접근 가능합니다.

### 예외 처리
- `ErrorCode` 인터페이스로 예외 상황별 HTTP 상태 코드와 메시지를 관리합니다. (`AuthErrorCode`, `MemberErrorCode` 구현)
- 공통 비즈니스 예외 `ApplicationException`을 던지고, `@ControllerAdvice`인 `GlobalExceptionHandler`가 일괄 처리합니다.
- 현재 처리 예외: 인증되지 않은 요청 / 유효하지 않거나 만료된 토큰(401), 존재하지 않는 회원(404), 기타 예외(400).

## 프로젝트 구조
```
roomescape/
├── RoomescapeApplication.java               # 메인 진입점
├── PageController.java                      # 뷰(HTML) 페이지 라우팅
│
├── auth/                                    # 인증/인가 (JWT)
│   ├── controller/
│   │   └── AuthController.java              # 로그인/로그아웃/로그인체크 API
│   ├── service/
│   │   └── AuthService.java
│   ├── domain/
│   │   └── LoginMember.java                 # 인증된 사용자 정보
│   ├── dto/
│   │   ├── LoginRequest.java
│   │   └── LoginCheckResponse.java
│   ├── exception/
│   │   └── AuthErrorCode.java
│   ├── jwt/                                 # JWT 토큰 메커니즘
│   │   ├── JwtTokenProvider.java            # 토큰 생성/검증
│   │   └── JwtTokenProperty.java            # JWT 설정값 (@ConfigurationProperties)
│   └── web/                                 # 웹 계층 확장(인증/인가 글루)
│       ├── Login.java                       # @Login 커스텀 애노테이션
│       ├── LoginMemberArgumentResolver.java # @Login LoginMember 주입
│       └── CheckAdminInterceptor.java       # 관리자 권한 검증 인터셉터
│
├── member/                                  # 회원
│   ├── controller/
│   │   └── MemberController.java
│   ├── service/
│   │   └── MemberService.java
│   ├── domain/
│   │   ├── Member.java                      # 도메인/엔티티
│   │   └── Role.java                        # 권한 enum (ADMIN/USER, isAdmin())
│   ├── dto/
│   │   ├── MemberRequest.java
│   │   └── MemberResponse.java
│   ├── exception/
│   │   └── MemberErrorCode.java
│   └── repository/
│       └── MemberDao.java
│
├── reservation/                             # 예약 (핵심 도메인)
│   ├── controller/
│   │   └── ReservationController.java
│   ├── service/
│   │   └── ReservationService.java
│   ├── domain/
│   │   └── Reservation.java
│   ├── dto/
│   │   ├── ReservationRequest.java
│   │   └── ReservationResponse.java
│   └── repository/
│       └── ReservationDao.java
│
├── theme/                                   # 테마
│   ├── controller/
│   │   └── ThemeController.java
│   ├── domain/
│   │   └── Theme.java
│   └── repository/
│       └── ThemeDao.java
│
├── time/                                    # 예약 시간
│   ├── controller/
│   │   └── TimeController.java
│   ├── service/
│   │   └── TimeService.java
│   ├── domain/
│   │   └── Time.java
│   ├── dto/
│   │   └── AvailableTime.java               # 예약 가능 시간 응답 모델
│   └── repository/
│       └── TimeDao.java
│
├── config/
│   └── WebConfig.java                       # ArgumentResolver + Interceptor 등록
│
├── exception/                               # 전역 예외 처리 (공통)
│   ├── GlobalExceptionHandler.java          # @RestControllerAdvice
│   ├── ApplicationException.java
│   ├── ErrorCode.java                       # 인터페이스 (AuthErrorCode/MemberErrorCode가 구현)
│   └── ErrorResponse.java
│
└── util/
    └── CookieUtil.java                      # 쿠키 추출/생성 헬퍼
```