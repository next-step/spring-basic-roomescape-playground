# Spring Core (배포)

<h3>7단계 - @Configuration</h3>

- [x] JWT 관련 로직을 roomescape와 같은 계층의 auth 패키지의 클래스로 분리
- [x] 불필요한 DB 접근 최소화
  - JWT 토큰에는 사용자 식별 정보와 권한 정보가 들어갑니다.
    만약 이 두 정보만 필요하다면 DB 접근이 필요하지 않습니다.