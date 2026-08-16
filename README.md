# 방탈출 예약 관리

이 저장소는 Spring MVC와 JPA를 공부하며 만든 방탈출 예약 서비스입니다. JDBC와 직접 작성한 DAO로 시작해 Spring Data JPA, 로그인 인증, 예약 대기 기능을 차례로 붙였습니다. 학습 단계마다 코드를 고쳐 온 흔적이 남아 있어 기능이 늘면서 구조가 어떻게 달라졌는지 살펴볼 수 있습니다.

## 주요 기능

- 회원가입과 로그인
- Access Token과 Refresh Token을 사용한 인증
- 관리자 전용 예약, 테마, 시간 관리
- 예약 생성과 멱등성 키를 이용한 중복 요청 방지
- 내 예약 목록 조회
- 예약 대기 등록과 대기 순번 조회
- 테마와 시간의 소프트 삭제
- 프로파일에 따른 초기 데이터 등록

## 기술 스택

- Java 17
- Spring Boot 3.1
- Spring MVC
- Spring Data JPA
- H2 Database
- JWT
- Thymeleaf
- Gradle
- RestAssured, JUnit 5, AssertJ

## 프로젝트 구조

```text
src/main/java
├── jwt                  JWT 생성과 파싱
└── roomescape
    ├── auth             인증, 인가, 쿠키 처리
    ├── exception        예외와 오류 코드
    ├── loader           실행 환경별 초기 데이터
    ├── member           회원
    ├── reservation      예약
    ├── theme            테마
    ├── time             예약 시간
    └── waiting          예약 대기
```

패키지는 기능 단위로 나눴습니다. 컨트롤러는 HTTP 요청과 응답을 받고 서비스는 유스케이스와 트랜잭션을 처리합니다. 데이터 접근은 `JpaRepository`를 상속한 저장소가 맡습니다.

## 실행 방법

Java 17이 필요합니다. 데이터베이스를 따로 설치하지 않아도 H2 인메모리 데이터베이스로 실행할 수 있습니다.

Windows:

```bash
gradlew.bat bootRun
```

macOS 또는 Linux:

```bash
./gradlew bootRun
```

실행 후 [http://localhost:8080](http://localhost:8080)으로 접속하면 됩니다. H2 콘솔 주소는 [http://localhost:8080/h2-console](http://localhost:8080/h2-console)입니다.

기본 계정은 다음과 같습니다.

| 구분 | 이메일 | 비밀번호 |
| --- | --- | --- |
| 관리자 | `admin@email.com` | `password` |
| 사용자 | `brown@email.com` | `password` |

## 초기 데이터

초기 데이터는 `schema.sql`이 아니라 `CommandLineRunner`로 등록합니다.

- 기본 환경에서는 `DataLoader`가 회원 정보를 등록합니다.
- `test` 프로파일에서는 `TestDataLoader`가 회원, 테마, 시간, 예약 데이터를 등록합니다.

테이블은 JPA 엔티티를 보고 Hibernate가 생성합니다. 현재 H2 인메모리 데이터베이스와 `create-drop` 설정을 사용하므로 애플리케이션을 다시 실행하면 데이터도 초기화됩니다.

## API

### 회원과 인증

| Method | URI | 설명 |
| --- | --- | --- |
| `POST` | `/members` | 회원가입 |
| `POST` | `/login` | 로그인 |
| `POST` | `/token/refresh` | Access Token 재발급 |
| `GET` | `/login/check` | 로그인 상태 확인 |
| `POST` | `/logout` | 로그아웃 |

### 예약과 대기

| Method | URI | 설명 |
| --- | --- | --- |
| `GET` | `/reservations` | 전체 예약 조회 |
| `POST` | `/reservations` | 예약 생성 |
| `DELETE` | `/reservations/{id}` | 예약 삭제 |
| `GET` | `/reservations-mine` | 내 예약과 예약 대기 조회 |
| `POST` | `/waitings` | 예약 대기 등록 |

### 테마와 시간

| Method | URI | 설명 |
| --- | --- | --- |
| `GET` | `/themes` | 테마 목록 조회 |
| `POST` | `/themes` | 테마 생성 |
| `DELETE` | `/themes/{id}` | 테마 삭제 |
| `GET` | `/times` | 시간 목록 조회 |
| `POST` | `/times` | 시간 생성 |
| `DELETE` | `/times/{id}` | 시간 삭제 |
| `GET` | `/available-times` | 날짜와 테마별 예약 가능 시간 조회 |

예약, 테마, 시간 관리 API 일부는 관리자 권한이 필요합니다.

## 구현하면서 바꾼 점

### Spring Data JPA

직접 `EntityManager`를 다루던 DAO를 `JpaRepository`로 바꿨습니다. 단순 조회에는 파생 쿼리를 씁니다. 여러 엔티티를 조인해 DTO로 반환하는 예약 조회에는 JPQL 생성자 표현식을 사용했습니다.

### 소프트 삭제

테마와 시간은 행을 바로 지우지 않고 `deleted` 상태만 바꿉니다. 예약을 생성할 때는 삭제되지 않은 테마와 시간만 조회합니다.

### 예약 대기 순번

같은 날짜, 테마, 시간에 먼저 등록된 대기 건수를 JPQL 서브쿼리로 계산합니다. 조회된 건수에 1을 더해 `1번째 예약대기`처럼 응답합니다.

### JWT

JWT 라이브러리를 직접 다루는 코드는 `roomescape` 패키지 밖의 `JwtUtils`에 모았습니다. 이 클래스에는 컴포넌트 어노테이션을 붙이지 않고 `JwtConfig`에서 빈으로 등록했습니다. 토큰에서 회원 식별 정보와 권한을 읽기 때문에 토큰 확인 과정에서는 회원 데이터베이스를 다시 조회하지 않습니다.

## 테스트

Windows:

```bash
gradlew.bat test
```

macOS 또는 Linux:

```bash
./gradlew test
```

테스트는 `test` 프로파일로 실행됩니다. 로그인, 예약 생성, 멱등성 키, 예약 대기 순번, JWT 설정을 REST API 기준으로 확인합니다.

