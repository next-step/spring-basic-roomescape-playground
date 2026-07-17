# 🚪 방탈출 예약 시스템

## 프로젝트 개요

방탈출 예약 관리를 위한 어드민 웹 애플리케이션입니다.
로그인부터 예약 추가까지 REST API로 구현하며, 권한 없는 요청에 대한 예외 처리를 포함합니다.
데이터 접근은 JPA(EntityManager) 기반으로 구성했습니다.


---

## 주요 기능

### 인증 / 인가
- 이메일·비밀번호로 로그인하면 **access token**을 발급해 `access-token` 쿠키로 내려줍니다.
- 함께 **refresh token**을 발급해 `refresh-token` 쿠키로 내리고, DB(`refresh_token`)에도 저장합니다. (사용자당 1개 유지)
- 쿠키의 access token으로 로그인 사용자 정보를 조회합니다 (`GET /login/check`).
- refresh token으로 access token을 재발급합니다 (`POST /login/refresh`). 재발급 시 refresh token도 함께 갱신(rotation)합니다.
- 로그아웃 시 access/refresh 쿠키를 만료시키고, DB의 refresh token을 삭제합니다.
- `HandlerMethodArgumentResolver`(`LoginMemberArgumentResolver`)와 `@Login`으로 컨트롤러에 로그인 사용자(`LoginMember`)를 주입합니다.
- `/admin/**`은 `CheckAdminInterceptor`로 ADMIN 권한을 가진 사용자만 접근 가능합니다.

### 데이터 접근 (JPA)
- Spring Data JPA가 아닌 **`EntityManager`를 직접 사용**해 영속성을 다룹니다.
- 각 도메인은 **저장소 인터페이스(`XxxRepository`) + `EntityManager` 기반 구현(`JpaXxxRepository`)** 으로 구성됩니다.
- 도메인 모델은 `entity/` 패키지의 JPA 엔티티로 매핑하며, 연관관계는 `@ManyToOne` 등으로 표현합니다.
- 스키마는 Hibernate가 엔티티로 생성(`spring.jpa.ddl-auto=create-drop`)하고, 초기 데이터는 `defer-datasource-initialization`으로 스키마 생성 후 삽입합니다.

### 예외 처리
- `ErrorCode` 인터페이스로 예외 상황별 HTTP 상태 코드와 메시지를 관리합니다. (`AuthErrorCode`, `MemberErrorCode` 구현)
- 공통 비즈니스 예외 `ApplicationException`을 던지고, `@ControllerAdvice`인 `GlobalExceptionHandler`가 일괄 처리합니다.
- 현재 처리 예외: 인증되지 않은 요청 / 유효하지 않거나 만료된 토큰(401), 권한 없음(403), 존재하지 않는 회원(404), 기타 예외(400).

## 프로젝트 구조
```
roomescape/
├── RoomescapeApplication.java                      # 메인 진입점
├── PageController.java                             # 뷰(HTML) 페이지 라우팅
│
├── auth/ # 인증/인가 (JWT + Refresh Token)
│ ├── controller/
│ │ └── AuthController.java                         # 로그인/로그아웃/로그인체크/토큰 재발급 API
│ ├── service/
│ │ └── AuthService.java
│ ├── domain/
│ │ └── LoginMember.java                            # 인증된 사용자 정보 (isAdmin())
│ ├── dto/
│ │ ├── LoginRequest.java
│ │ ├── LoginCheckResponse.java
│ │ └── TokenResponse.java                          # access/refresh 토큰 응답
│ ├── entity/
│ │ └── RefreshToken.java                           # refresh token JPA 엔티티
│ ├── exception/
│ │ └── AuthErrorCode.java
│ ├── jwt/ # JWT 토큰 메커니즘
│ │ ├── JwtTokenProvider.java                       # 토큰 생성/검증
│ │ └── JwtTokenProperty.java                       # JWT 설정값 (@ConfigurationProperties)
│ ├── repository/
│ │ ├── RefreshTokenRepository.java                 # 저장소 인터페이스
│ │ └── JpaRefreshTokenRepository.java              # EntityManager 기반 구현
│ └── web/ # 웹 계층 확장(인증/인가 글루)
│ ├── Login.java # @Login 커스텀 애노테이션
│ ├── LoginMemberArgumentResolver.java              # @Login LoginMember 주입
│ ├── TokenExtractor.java                           # 쿠키에서 토큰 추출(+인증 실패 처리)
│ └── CheckAdminInterceptor.java                    # 관리자 권한 검증 인터셉터
│
├── member/ # 회원
│ ├── controller/
│ │ └── MemberController.java
│ ├── service/
│ │ └── MemberService.java
│ ├── entity/
│ │ ├── Member.java                                 # JPA 엔티티
│ │ └── Role.java                                   # 권한 enum (ADMIN/USER, isAdmin())
│ ├── dto/
│ │ ├── MemberRequest.java
│ │ └── MemberResponse.java
│ ├── exception/
│ │ └── MemberErrorCode.java
│ └── repository/
│ ├── MemberRepository.java                         # 저장소 인터페이스
│ └── JpaMemberRepository.java                      # EntityManager 기반 구현
│
├── reservation/ # 예약 (핵심 도메인)
│ ├── controller/
│ │ └── ReservationController.java
│ ├── service/
│ │ └── ReservationService.java
│ ├── entity/
│ │ └── Reservation.java                            # JPA 엔티티 (member/time/theme @ManyToOne)
│ ├── dto/
│ │ ├── ReservationRequest.java
│ │ ├── ReservationResponse.java
│ │ └── MyReservationResponse.java                  # 내 예약 조회 응답
│ └── repository/
│ ├── ReservationRepository.java
│ └── JpaReservationRepository.java
│
├── theme/ # 테마
│ ├── controller/
│ │ └── ThemeController.java
│ ├── service/
│ │ └── ThemeService.java
│ ├── entity/
│ │ └── Theme.java
│ ├── dto/
│ │ ├── ThemeRequest.java
│ │ └── ThemeResponse.java
│ └── repository/
│ ├── ThemeRepository.java
│ └── JpaThemeRepository.java
│
├── time/ # 예약 시간
│ ├── controller/
│ │ └── TimeController.java
│ ├── service/
│ │ └── TimeService.java
│ ├── entity/
│ │ └── Time.java
│ ├── dto/
│ │ ├── TimeRequest.java
│ │ ├── TimeResponse.java
│ │ └── AvailableTime.java                          # 예약 가능 시간 응답 모델
│ └── repository/
│ ├── TimeRepository.java
│ └── JpaTimeRepository.java
│
├── config/
│ └── WebConfig.java                                # ArgumentResolver + Interceptor 등록
│
├── exception/                                      # 전역 예외 처리 (공통)
│ ├── GlobalExceptionHandler.java                   # @RestControllerAdvice
│ ├── ApplicationException.java
│ ├── ErrorCode.java                                # 인터페이스 (AuthErrorCode/MemberErrorCode가 구현)
│ └── ErrorResponse.java
│
└── util/
└── CookieUtil.java # 쿠키 추출/생성 헬퍼
```