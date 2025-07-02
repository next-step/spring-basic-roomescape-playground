# 인증 단계 정리

## 1단계 - 로그인 기능 구현

- 사용자는 `/login` 로 email과 password를 전송하여 로그인
- 로그인 성공 시 JWT 토큰을 생성해 쿠키(`token`)에 저장하여 응답
- 토큰에는 사용자 식별 정보(id, name, role)를 포함
- JWT 토큰은 `JwtProvider`에서 생성

## 2단계 - 인증 정보 활용

- 쿠키에서 토큰을 추출하고 사용자 정보를 조회하는 `AuthService` 구현
- 컨트롤러 메서드에서 `LoginMember`를 바로 사용할 수 있도록 `LoginMemberArgumentResolver` 구현
- 예: 예약 요청 시, name이 없으면 로그인한 사용자의 이름으로 예약 처리
- 인증 정보가 필요한 로직을 깔끔하게 분리하여 재사용성 및 테스트 용이성 향상

## 3단계 - 관리자 권한 처리

- 관리자만 접근 가능한 경로(`/admin)를 `AdminAuthorizationInterceptor`로 보호
- 토큰 기반으로 사용자의 role 정보를 조회하고, ADMIN이 아닐 경우 401(Unauthorized) 응답
- 인터셉터는 `WebConfig`에서 등록

---
# JPA(EntityManager 중심으로)
## 4단계 - JPA 전환
- `spring-boot-starter-jdbc` 의존성 제거 후 `spring-boot-starter-data-jpa`로 교체
- DAO 제거, EntityManager 기반 Repository 직접 구현
- Entity 클래스에 `@Id`, `@GeneratedValue` 설정
- `Theme`, `Time` → 단일 객체로 우선 Entity화
- `Reservation`, `Waiting` → `@ManyToOne`, `@JoinColumn`으로 연관관계 매핑
- `schema.sql`과 함께 `spring.jpa.ddl-auto=create-drop`으로 매핑 확인

##  5단계 - 내 예약 목록 조회
- `GET /reservations-mine` API 구현
- 로그인 사용자의 예약 정보를 `LoginMember`를 통해 추출
- 예약 정보는 `MyReservationResponse` DTO로 변환해 전달
- `Reservation.name`은 직접 입력이 아닌 `LoginMember.name()`을 통해 설정

## 6단계 - 예약 대기 기능
- 예약이 모두 찼을 경우 `POST /waitings`로 예약 대기 신청 가능
- 예약 대기 Entity `Waiting`과 DTO `WaitingResponse`, `WaitingWithRank` 추가
- `GET /reservations-mine` 응답에 대기 상태도 함께 포함
- `status`: `"예약"` 또는 `"1번째 예약대기"` 형식으로 구분
- JPQL의 서브쿼리를 이용해 대기 순번을 계산
