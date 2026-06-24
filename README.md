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
