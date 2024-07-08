# Spring Core (배포)

<h3>7단계 - @Configuration</h3>

- [x] JWT 관련 로직을 roomescape와 같은 계층의 auth 패키지의 클래스로 분리
- [x] 불필요한 DB 접근 최소화
  - JWT 토큰에는 사용자 식별 정보와 권한 정보가 들어갑니다.
    만약 이 두 정보만 필요하다면 DB 접근이 필요하지 않습니다.


<h3>8단계 - Profile과 Resource</h3>

schema.sql 대신 데이터베이스를 초기화 클래스 생성
- [x] Production용도 DataLoader 생성
  사용자 정보만 초기화
- [x] Test용도 TestDataLoader생성
  테스트에 필요한 사전 값 초기화
- [x] Environemt 분리 (토큰 비밀키)