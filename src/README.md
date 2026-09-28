# 방탈출 예약 관리 Spring MVC (인증)
---
방탈출 예약 관리 서비스에 JWT 기반 인증, 커스텀 ArgumentResolver를 통한 인증 객체 주입, 그리고 HandlerInterceptor를 활용한 역할 기반 접근 제어를 단계별로 구축한 미션입니다.

### 단계별 구현 기능 목록
---
** 1단계**: JWT 기반 로그인 및 회원 가입
- 로그인 및 JWT 발급:
    - `POST /login` 요청 시 전달 받은 값들을 검증 후 `id`, `name`, `role`을 Claim에 담은 JWT 생성
    - 생성된 토큰을 쿠키로 클라이언트에 전달
- 로그인 상태 확인:
    - `GET /login/check`를 통해 현재 로그인한 사용자의 정보 응답하도록 구현
---
** 2단계**: ArgumentResolver를 활용한 사용자 주입 및 예약 분기
- `HandlerMethodArgumentResolver` 구현:
    - `LoginMemberArgumentResolver`를 작성하여 컨트롤러 메서드 파라미터로
       `LoginMember`를 선언하면 쿠키의 JWT를 자동 파싱하여 주입하도록 구현
- `WebMvcConfig` 설정 등록:
    - 스프링의 `WebMvcConfig`에 `addArgumentResolvers`를 오버라이드하여 커스텀 리졸버 등록
- 예약자 이름 분기 처리:
    - name이 명시된 경우에는 관리자 대리 예약으로 판단하여 해당 이름을 예약자로 저장
    - 요청 본문에 name이 비어있는 경우에는 주입받은 `LoginMember`의 이름을 기본 예약자로 지정
---
**3단계**: HandlerInterceptor를 통한 관리자 페이지 권한 인가
- AdminInterceptor 구현
    - 클라이언트 요청이 컨트롤러에 도달하기 전 `preHandler` 단계에서 쿠키 토큰 검증
    - 토큰 내 `role`이 `ADMIN`이 아닌 `USER`인 경우`401 Unauthorized` 상태 코드 반환 및 false 리턴
    - `ADMIN` 권한이 확인된 경우에만 컨트롤러 진입 허용
---
**4단계**: Spring Data JPA 기반 엔티티 및 도메인 전환
- JPA 엔티티 및 연관관계 매핑:
  - `Member`, `Theme`, `ReservationTime`, `Reservation` 도메인을 JPA 엔티티로 전환
  - 기존 JDBC 기반 DAO 계층을 Spring Data JPA의 `CrudRepository` 기반 Repository로 교체
---
**5단계**: 내 예약 목록 조회 API 구현
- 사용자별 예약 목록 조회:
  - `GET /reservations-mine`통해 로그인한 사용자의 예약 내역 목록 반환
  - `ReservationRepository`에 `findByMemberId` 쿼리 메서드를 정의하여 사용자 식별자 기반 조회 수행
- 응답 DTO:
  - 테마명, 날짜, 시간, 상태(`"예약"`)를 담는 `MyReservationResponse` DTO 적용
---
**6단계**: 예약 대기 및 순위 조회 시스템 구현
- 예약 대기(Waiting) 도메인:
  - 예약 대기 등록(`POST /waitings`) 및 대기 취소(`DELETE /waitings/{id}`) API 구현
  - 동일 조건(날짜, 시간, 테마, 회원) 중복 대기 신청 방지 검증 로직 추가
  - 대기 취소 시 본인의 대기 내역만 삭제할 수 있도록 사용자 인가 검증
- JPQL 서브쿼리를 통한 대기 순번 계산:
  - `WaitingRepository`에서 서브쿼리(`COUNT`)를 활용해 동일 조건 내 앞선 대기 건수를 계산하는 `findWaitingsWithRankByMemberId` 쿼리 작성
  - 쿼리 결과를 바인딩하기 위한 `WaitingWithRank` DTO 구현
- 내 예약 목록 통합 응답:
  - `GET /reservations-mine` 요청 시 확정 예약 목록과 대기 목록을 함께 병합하여 반환
  - 계산된 순위에 맞춰 대기 건의 상태를 `"N번째 예약대기"`으로 제공
