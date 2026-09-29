## JPA 적용

### JPA 기반 데이터 접근 기능

- [x] 기존 JDBC 기반 데이터 접근 방식을 Spring Data JPA 기반으로 변경한다.
- [x] `Member`, `Reservation`, `Theme`, `Time`, `Waiting`을 JPA Entity로 관리한다.
- [x] 각 Entity의 식별자에 `@Id`와 `@GeneratedValue`를 적용한다.
- [x] 데이터베이스 테이블과 Entity를 `@Table`을 이용해 매핑한다.
- [x] JPA Entity 생성을 위해 기본 생성자를 제공한다.
- [x] Repository가 `JpaRepository`를 상속하도록 구성한다.
- [x] Service 계층에 트랜잭션 경계를 설정해 JPA 영속성 컨텍스트의 생명주기를 관리한다.
- [x] 조회 작업에는 읽기 전용 트랜잭션을 적용하고 데이터 변경 작업에는 일반 트랜잭션을 적용한다.

---

## `MemberRepository`

### 회원 데이터 접근 기능

- [x] `JpaRepository<Member, Long>`을 상속해 회원 데이터를 관리한다.
- [x] `findByEmail()`을 이용해 이메일을 기준으로 회원을 조회한다.
- [x] `findByName()`을 이용해 이름을 기준으로 회원을 조회한다.
- [x] `findById()`를 이용해 회원 식별자를 기준으로 회원을 조회한다.
- [x] 회원 저장 시 JPA의 `save()`를 이용한다.

---

## `Theme`

### JPA Entity 및 삭제 상태 관리 기능

- [x] `Theme`을 JPA Entity로 관리한다.
- [x] 테마 삭제 여부를 `deleted` 필드로 관리한다.
- [x] 실제 데이터를 삭제하지 않고 `deleted` 값을 `true`로 변경하는 방식으로 처리한다.

---

## `ThemeRepository`

### 테마 데이터 접근 기능

- [x] `JpaRepository<Theme, Long>`을 상속한다.
- [x] `findAllByDeletedFalse()`를 이용해 삭제되지 않은 테마만 조회한다.
- [x] JPA의 `save()`를 이용해 테마를 저장한다.
- [x] 식별자를 이용해 특정 테마를 조회한다.
- [x] 현재 미션에서는 soft delete 조건을 명시적으로 확인할 수 있도록 `findAllByDeletedFalse()` 방식을 유지한다.

---

## `ThemeRequest`

### 테마 요청 데이터 관리 기능

- [x] Controller에서 `Theme` Entity를 직접 요청 객체로 사용하지 않도록 분리한다.
- [x] 테마 이름을 전달받는다.
- [x] 테마 설명을 전달받는다.
- [x] API 입력 모델과 Persistence 모델의 역할을 분리한다.

---

## `ThemeResponse`

### 테마 응답 데이터 관리 기능

- [x] Controller에서 `Theme` Entity를 직접 응답하지 않도록 분리한다.
- [x] 테마 식별자를 응답한다.
- [x] 테마 이름을 응답한다.
- [x] 테마 설명을 응답한다.
- [x] Entity 내부 필드나 연관관계 변경이 API 응답에 직접 영향을 주지 않도록 구성한다.

---

## `Time`

### JPA Entity 및 삭제 상태 관리 기능

- [x] `Time`을 JPA Entity로 관리한다.
- [x] 실제 시간 값을 `time_value` 컬럼과 매핑한다.
- [x] 삭제 여부를 `deleted` 필드로 관리한다.
- [x] 실제 데이터를 삭제하지 않고 `deleted` 값을 `true`로 변경하는 방식으로 처리한다.
- [x] 기존 API에서 사용하는 시간 값을 `value`로 관리한다.
- [x] JPA 테스트에서 시간 값을 확인할 수 있도록 필요한 접근 메서드를 제공한다.

---

## `TimeRepository`

### 시간 데이터 접근 기능

- [x] `JpaRepository<Time, Long>`을 상속한다.
- [x] `findAllByDeletedFalse()`를 이용해 삭제되지 않은 시간만 조회한다.
- [x] JPA의 `save()`를 이용해 시간을 저장한다.
- [x] 식별자를 이용해 특정 시간을 조회한다.
- [x] 현재 미션에서는 soft delete 조건을 명시적으로 확인할 수 있도록 `findAllByDeletedFalse()` 방식을 유지한다.

---

## `TimeRequest`

### 시간 요청 데이터 관리 기능

- [x] Controller에서 `Time` Entity를 직접 요청 객체로 사용하지 않도록 분리한다.
- [x] 예약 시간 값을 전달받는다.
- [x] API 입력 모델과 Persistence 모델의 역할을 분리한다.

---

## `TimeResponse`

### 시간 응답 데이터 관리 기능

- [x] Controller에서 `Time` Entity를 직접 응답하지 않도록 분리한다.
- [x] 시간 식별자를 응답한다.
- [x] 시간 값을 응답한다.
- [x] 기존 API 응답 형식을 유지한다.
- [x] Entity 변경이 API 응답 구조에 직접 영향을 주지 않도록 구성한다.

---

## `Reservation`

### JPA 연관관계 관리 기능

- [x] `Reservation`을 JPA Entity로 관리한다.
- [x] `Time`과 `ManyToOne` 관계를 설정한다.
- [x] `Theme`과 `ManyToOne` 관계를 설정한다.
- [x] 로그인 회원의 예약을 관리하기 위해 `Member`와 `ManyToOne` 관계를 설정한다.
- [x] `Member`, `Theme`, `Time` 연관관계의 Fetch 전략을 `LAZY`로 설정한다.
- [x] 필요한 조회에서만 Fetch Join을 이용해 연관 데이터를 함께 조회한다.
- [x] `member_id` 외래 키를 이용해 예약 회원을 저장한다.
- [x] 관리자가 예약을 생성하는 경우 기존 `name` 값을 이용할 수 있도록 구성한다.
- [x] 일반 사용자가 예약하는 경우 로그인한 회원 정보를 이용해 `member_id`를 저장한다.
- [x] 동일한 날짜, 시간, 테마 조합이 저장되지 않도록 DB Unique Constraint를 설정한다.

---

## `ReservationRepository`

### 예약 데이터 접근 기능

- [x] `JpaRepository<Reservation, Long>`을 상속한다.
- [x] 날짜와 테마를 기준으로 예약 목록을 조회한다.
- [x] 회원 식별자를 이용해 해당 회원의 예약 목록을 조회한다.
- [x] 날짜, 시간, 테마가 동일한 예약의 존재 여부를 조회한다.
- [x] 회원 예약 조회 시 실제 응답에 필요한 `Theme`, `Time`을 Fetch Join으로 조회한다.
- [x] 관리자 전체 예약 조회 시 `Member`, `Theme`, `Time`을 함께 조회한다.
- [x] 관리자에 의해 생성된 예약은 `Member`가 없을 수 있으므로 `Member` 조회에는 Left Fetch Join을 사용한다.
- [x] 예약 가능 시간 조회 시 필요한 `Time`을 Fetch Join으로 조회한다.
- [x] 예약 대기 순번 생성 시 동일 예약 슬롯을 잠글 수 있도록 비관적 쓰기 락 조회 기능을 제공한다.

---

## 중복 예약 방지

### 애플리케이션 및 데이터베이스 중복 방지 기능

- [x] 예약 생성 전 동일한 날짜, 시간, 테마의 예약이 존재하는지 확인한다.
- [x] 사전 검증을 통해 일반적인 중복 예약 요청을 빠르게 차단한다.
- [x] 동시에 여러 예약 요청이 들어오는 경우를 대비해 DB Unique Constraint를 적용한다.
- [x] `date`, `time_id`, `theme_id` 조합의 중복 저장을 데이터베이스에서 최종적으로 방지한다.
- [x] 저장 시 Unique Constraint 위반이 발생하면 `DataIntegrityViolationException`을 처리한다.
- [x] DB 예외를 `DuplicateReservationException`으로 변환한다.
- [x] 중복 예약 요청에 대해 `409 Conflict`를 응답한다.

---

## `MyReservationResponse`

### 내 예약 목록 응답 기능

- [x] 내 예약 목록 조회에 필요한 공통 응답 정보를 관리한다.
- [x] 예약 또는 예약 대기 객체의 식별자를 공통된 의미의 `id`로 응답한다.
- [x] `Reservation.id`는 예약 객체의 식별자로 사용한다.
- [x] `Waiting.id`는 예약 대기 객체의 식별자로 사용한다.
- [x] 테마 이름을 응답한다.
- [x] 예약 날짜를 응답한다.
- [x] 예약 시간을 응답한다.
- [x] 예약 상태를 `status`로 응답한다.
- [x] 실제 예약인 경우 상태를 `"예약"`으로 응답한다.
- [x] 예약 대기인 경우 현재 대기 순서를 포함한 상태를 응답한다.
- [x] `reservationId`처럼 특정 도메인에 종속된 이름 대신 두 객체에서 공통으로 사용할 수 있는 `id`를 사용한다.

---

## `ReservationController`

### 내 예약 목록 조회 기능

- [x] `GET /reservations-mine` 요청을 처리한다.
- [x] `LoginMember`를 이용해 현재 로그인한 회원을 식별한다.
- [x] 내 예약 목록 조회를 `ReservationService`에 위임한다.
- [x] 로그인 회원의 예약과 예약 대기 목록을 함께 응답한다.

---

## `ReservationService`

### 내 예약 목록 조회 기능

- [x] 로그인 회원의 식별자를 이용해 예약 목록을 조회한다.
- [x] 조회한 예약을 `MyReservationResponse`로 변환한다.
- [x] 실제 예약의 상태를 `"예약"`으로 설정한다.
- [x] 로그인 회원의 예약 대기 목록도 함께 조회한다.
- [x] 예약 목록과 예약 대기 목록을 하나의 목록으로 합쳐 반환한다.
- [x] 예약 대기 순위를 이용해 `"1번째 예약대기"` 형식의 상태를 생성한다.
- [x] 예약과 예약 대기의 식별자는 `MyReservationResponse.id`라는 공통 의미로 전달한다.

### 중복 예약 방지 기능

- [x] 예약 생성 전 동일한 날짜, 시간, 테마의 예약이 존재하는지 확인한다.
- [x] 이미 예약된 날짜, 시간, 테마에는 새로운 예약을 생성하지 않는다.
- [x] 애플리케이션의 사전 검증과 DB Unique Constraint를 함께 사용한다.
- [x] 동시 요청으로 사전 검증을 모두 통과하더라도 DB에서 중복 저장을 최종적으로 방지한다.
- [x] `saveAndFlush()` 시 발생하는 중복 저장 예외를 `DuplicateReservationException`으로 변환한다.

### 트랜잭션 관리 기능

- [x] `ReservationService`의 기본 트랜잭션을 `@Transactional(readOnly = true)`로 설정한다.
- [x] 조회 메서드는 읽기 전용 트랜잭션으로 실행한다.
- [x] 예약 생성과 같이 데이터를 변경하는 메서드에는 별도의 `@Transactional`을 적용한다.
- [x] 예약 삭제와 같이 데이터를 변경하는 메서드에는 별도의 `@Transactional`을 적용한다.
- [x] 예약 생성 과정의 중복 확인, 연관 Entity 조회, 예약 저장을 하나의 트랜잭션으로 처리한다.
- [x] Service의 하나의 비즈니스 작업을 기준으로 트랜잭션 경계를 설정한다.
- [x] JPA의 영속성 컨텍스트가 트랜잭션 범위 안에서 Entity를 관리하도록 구성한다.
- [x] 조회 작업과 변경 작업의 트랜잭션 목적을 구분한다.
- [x] `readOnly = true`를 통해 조회 작업이라는 의도를 명확하게 표현한다.
- [x] `@Transactional`과 DB Unique Constraint 및 Lock의 역할을 구분한다.
- [x] 트랜잭션은 작업의 원자성을 관리하고, 중복 예약은 DB Unique Constraint를 통해 방지한다.
- [x] 예약 대기 순번의 동시성 문제는 비관적 락을 통해 제어한다.

---

## `Waiting`

### 예약 대기 정보 관리 기능

- [x] `Waiting`을 JPA Entity로 관리한다.
- [x] 예약 대기 Entity의 식별자 `id`를 자동 생성한다.
- [x] `id`는 예약 대기 객체를 식별하는 용도로만 사용한다.
- [x] 예약 대기를 요청한 회원의 식별자를 `memberId`로 저장한다.
- [x] 현재 요구사항에서는 회원 객체의 상세 정보가 필요하지 않아 `Member` 연관관계 대신 `memberId`를 사용한다.
- [x] 예약 대기 날짜를 저장한다.
- [x] 예약 시간과 `ManyToOne` 관계를 설정한다.
- [x] 예약 테마와 `ManyToOne` 관계를 설정한다.
- [x] 예약 대기 신청 순서를 나타내는 `waitingOrder`를 별도로 관리한다.
- [x] Entity 식별자와 예약 대기 순서의 도메인 의미를 분리한다.
- [x] 동일한 날짜, 시간, 테마에서 같은 `waitingOrder`가 중복 저장되지 않도록 Unique Constraint를 설정한다.

---

## `WaitingRequest`

### 예약 대기 요청 데이터 관리 기능

- [x] 예약 대기 날짜를 전달받는다.
- [x] 예약 대기 시간 식별자를 전달받는다.
- [x] 예약 대기 테마 식별자를 전달받는다.
- [x] 로그인 회원 정보는 요청 본문에서 받지 않고 `LoginMember`를 이용한다.

---

## `WaitingResponse`

### 예약 대기 생성 응답 기능

- [x] 생성된 예약 대기의 식별자를 응답한다.
- [x] 예약 대기 테마 이름을 응답한다.
- [x] 예약 대기 날짜를 응답한다.
- [x] 예약 대기 시간을 응답한다.

---

## `WaitingWithRank`

### 예약 대기 순위 관리 기능

- [x] `Waiting` 객체와 현재 예약 대기 순위를 함께 관리한다.
- [x] 예약 대기 목록 조회 시 각 대기의 순위 정보를 전달한다.
- [x] 같은 테마, 날짜, 시간에 자신보다 작은 `waitingOrder`를 가진 현재 대기자의 수를 이용해 순위를 계산한다.
- [x] 저장된 `waitingOrder`와 현재 대기열에서의 `rank`를 서로 다른 의미로 관리한다.

---

## `WaitingRepository`

### 예약 대기 데이터 접근 기능

- [x] `JpaRepository<Waiting, Long>`을 상속한다.
- [x] 회원이 동일한 예약에 이미 대기 중인지 확인한다.
- [x] 예약 대기 식별자와 회원 식별자를 이용해 본인의 예약 대기를 조회한다.
- [x] JPQL을 이용해 회원의 예약 대기 목록과 순위 정보를 함께 조회한다.
- [x] 동일한 테마, 날짜, 시간에서 자신보다 작은 `waitingOrder`를 가진 현재 대기자의 수를 계산한다.
- [x] 예약 대기 순위를 Entity의 `id`가 아닌 `waitingOrder`를 기준으로 계산한다.
- [x] 동일한 예약 조건의 최대 `waitingOrder`를 조회한다.
- [x] 새로운 예약 대기 생성 시 `MAX(waitingOrder) + 1`을 다음 신청 순서로 사용한다.

---

## `WaitingService`

### 예약 대기 생성 기능

- [x] 예약 대기 요청 시 해당 날짜, 시간, 테마에 기존 예약이 존재하는지 확인한다.
- [x] 예약이 존재하지 않는 경우 예약 대기를 생성하지 않는다.
- [x] 동일한 회원이 같은 예약에 중복으로 대기하는 것을 방지한다.
- [x] 요청한 테마와 시간을 Repository에서 조회한다.
- [x] 로그인 회원의 식별자를 이용해 `Waiting`을 생성한다.
- [x] 현재 최대 `waitingOrder`에 1을 더해 새로운 대기 신청 순서를 생성한다.
- [x] 생성된 예약 대기를 `WaitingResponse`로 변환해 반환한다.

### 예약 대기 동시성 제어 기능

- [x] 대기 신청 과정을 하나의 트랜잭션으로 처리한다.
- [x] 동일한 날짜, 시간, 테마에 대한 예약 데이터를 `PESSIMISTIC_WRITE` 방식으로 잠근다.
- [x] 동일한 예약 슬롯에 여러 대기 신청이 동시에 들어오더라도 순번 계산을 순차적으로 수행한다.
- [x] 비관적 락을 획득한 뒤 현재 최대 `waitingOrder`를 조회한다.
- [x] DB Unique Constraint를 이용해 동일 예약 슬롯에 같은 대기 순서가 저장되는 것을 추가로 방지한다.

### 예약 대기 취소 기능

- [x] 예약 대기 식별자와 로그인 회원 식별자를 이용해 예약 대기를 조회한다.
- [x] 본인이 생성한 예약 대기만 취소할 수 있도록 구성한다.
- [x] 조회된 예약 대기를 Repository에서 삭제한다.
- [x] 존재하지 않는 예약 대기인 경우 예외를 발생시킨다.

### 예약 대기 순위 조회 기능

- [x] 로그인 회원의 예약 대기 목록을 조회한다.
- [x] `WaitingWithRank`를 이용해 예약 대기 정보와 순위를 함께 전달한다.
- [x] 자신보다 작은 `waitingOrder`를 가진 동일 조건의 현재 대기자 수에 1을 더해 실제 대기 순위를 계산한다.
- [x] 앞선 대기자가 취소되더라도 기존 `waitingOrder`를 수정하지 않고 현재 순위를 다시 계산한다.

---

## `WaitingController`

### 예약 대기 요청 기능

- [x] `POST /waitings` 요청을 처리한다.
- [x] `WaitingRequest`와 `LoginMember`를 전달받는다.
- [x] 예약 대기 생성을 `WaitingService`에 위임한다.
- [x] 예약 대기 생성 성공 시 `201 Created`를 응답한다.
- [x] 생성된 예약 대기 정보를 응답 본문에 포함한다.
- [x] `Location` 헤더에 `/waitings/{id}` 경로를 포함한다.

### 예약 대기 취소 기능

- [x] `DELETE /waitings/{id}` 요청을 처리한다.
- [x] `LoginMember`를 이용해 현재 로그인 회원을 확인한다.
- [x] 예약 대기 취소를 `WaitingService`에 위임한다.
- [x] 예약 대기 취소 성공 시 `204 No Content`를 응답한다.

---

## 예약 대기 처리 흐름

### 예약 대기 요청

- [x] 로그인 회원이 이미 예약된 날짜, 테마, 시간을 선택해 예약 대기를 요청한다.
- [x] `POST /waitings`를 통해 예약 대기 정보를 전달한다.
- [x] 동일 예약 슬롯의 Reservation에 비관적 쓰기 락을 획득한다.
- [x] 기존 예약이 존재하는지 확인한다.
- [x] 동일 회원의 중복 예약 대기 여부를 확인한다.
- [x] 현재 동일 예약 슬롯의 최대 `waitingOrder`를 조회한다.
- [x] 최대 `waitingOrder`에 1을 더해 새로운 대기 신청 순서를 결정한다.
- [x] 조건을 만족하면 새로운 예약 대기를 저장한다.
- [x] 트랜잭션이 종료되면 해당 예약 슬롯의 락을 해제한다.

### 내 예약 목록 조회

- [x] `GET /reservations-mine`을 이용해 로그인 회원의 예약 정보를 조회한다.
- [x] 실제 예약 목록과 예약 대기 목록을 함께 조회한다.
- [x] 예약과 예약 대기의 식별자를 공통된 `id` 필드로 응답한다.
- [x] 실제 예약은 `"예약"` 상태로 표시한다.
- [x] 예약 대기는 `"1번째 예약대기"`와 같이 현재 대기 순서를 포함해 표시한다.
- [x] 대기 순위는 `waitingOrder`를 기준으로 현재 남아 있는 대기자들을 다시 계산한다.

### 예약 대기 취소

- [x] 내 예약 목록의 예약 대기 항목에서 취소 요청을 할 수 있다.
- [x] `DELETE /waitings/{id}`를 이용해 예약 대기를 삭제한다.
- [x] 취소 요청 시 로그인 회원 본인의 예약 대기인지 확인한다.
- [x] 앞선 대기가 취소되더라도 다른 Waiting의 `waitingOrder` 값은 직접 수정하지 않는다.
- [x] 이후 조회 시 남아 있는 대기자를 기준으로 현재 대기 순위를 다시 계산한다.

---
