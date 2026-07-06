## 공통

JWT 서명 키는 환경변수로 관리합니다.

```bash
export JWT_SECRET_KEY=충분히_긴_랜덤_시크릿_키
```

로컬 실행에서는 `.env` 파일을 사용할 수 있습니다.

```properties
JWT_SECRET_KEY=충분히_긴_랜덤_시크릿_키
```

`.env`는 Git에 포함되지 않습니다.

## 예약 생성

```http
POST /reservations
Content-Type: application/json
Cookie: token={jwt}
```

### 요청

```json
{
  "name": "브라운",
  "date": "2026-07-01",
  "time": 1,
  "theme": 1
}
```

### 응답

```http
HTTP/1.1 201 Created
Content-Type: application/json
Location: /reservations/{id}
```

```json
{
  "id": 1,
  "name": "브라운",
  "theme": "테마1",
  "date": "2026-07-01",
  "time": "10:00"
}
```

### 변경된 규칙

- `name`이 있으면 관리자 화면에서 생성한 예약으로 보고 `name` 문자열을 저장합니다.
- `name`이 없으면 로그인한 회원의 `member_id`를 예약에 저장합니다.
- 같은 `date`, `theme`, `time` 조합의 중복 예약은 생성할 수 없습니다.
- 로그인 정보 없이 `name`도 없으면 `401 Unauthorized`를 응답합니다.

## 내 예약 목록 조회

```http
GET /reservations-mine
Cookie: token={jwt}
```

### 응답

```http
HTTP/1.1 200 OK
Content-Type: application/json
```

```json
[
  {
    "reservationId": 1,
    "theme": "테마1",
    "date": "2024-03-01",
    "time": "10:00",
    "status": "예약"
  },
  {
    "reservationId": 4,
    "theme": "테마1",
    "date": "2024-03-01",
    "time": "10:00",
    "status": "1번째 예약대기"
  }
]
```

### 변경된 규칙

- 로그인한 회원의 예약 목록과 예약 대기 목록을 함께 반환합니다.
- 예약은 `status`가 `"예약"`입니다.
- 예약 대기는 `"1번째 예약대기"`, `"2번째 예약대기"`처럼 순번을 포함합니다.
- 예약 대기 순번은 같은 날짜, 테마, 시간의 대기 중 먼저 생성된 대기 수를 기준으로 계산합니다.

## 예약 대기 생성

```http
POST /waitings
Content-Type: application/json
Cookie: token={jwt}
```

### 요청

```json
{
  "date": "2024-03-01",
  "time": 1,
  "theme": 1
}
```

### 응답

```http
HTTP/1.1 201 Created
Content-Type: application/json
Location: /waitings/{id}
```

```json
{
  "id": 1,
  "theme": "테마1",
  "date": "2024-03-01",
  "time": "10:00"
}
```

### 규칙

- 로그인 사용자만 예약 대기를 생성할 수 있습니다.
- 이미 예약이 존재하는 날짜, 테마, 시간에 대해서만 대기를 생성할 수 있습니다.
- 같은 사용자가 같은 날짜, 테마, 시간에 중복 대기할 수 없습니다.
- 이미 해당 날짜, 테마, 시간을 예약한 사용자는 같은 슬롯에 대기할 수 없습니다.

## 예약 대기 취소

```http
DELETE /waitings/{id}
Cookie: token={jwt}
```

### 응답

```http
HTTP/1.1 204 No Content
```

### 규칙

- 로그인 사용자만 예약 대기를 취소할 수 있습니다.
- 본인이 생성한 예약 대기만 취소할 수 있습니다.

## 화면 변경

```http
GET /reservation-mine
```

- 내 예약 목록 페이지를 반환합니다.
- 페이지는 `GET /reservations-mine` API를 호출합니다.
- `status`가 `"예약"`이 아니면 예약 대기 취소 버튼을 표시합니다.
- 취소 버튼은 `DELETE /waitings/{reservationId}`를 호출합니다.

## 테스트 범위

- 로그인 및 JWT 쿠키 발급
- 관리자 페이지 권한 검증
- 예약 생성 시 로그인 사용자와 이름 요청 처리
- JPA Repository 저장/조회
- 내 예약 목록 조회
- 예약 대기 생성
- 예약 대기 취소
- 예약 대기 순번 표시
