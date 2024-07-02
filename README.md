# Spring Data JPA 

<h3>4단계 JPA 전환</h3>

- [x] gradle 의존성 추가
- [x] 엔티티 매핑
- [x] 연관관계 매핑

<h3>5단계 내 예약 목록 조회</h3>

내 예약 목록을 조회 API 구현 
- [x]  Reservation 테이블의 수정 
  - 관리자가 어드민 화면에서 예약을 생성하는 경우 name 값을 string으로 전달 
  - 사용자가 예약 화면에서 예약 생성하는 경우 로그인 정보를 이용하여 Member의 ID를 Reservation에 저장하도록 합니다. 
- [x] 화면 응답 (reservation-mine.html.html)

<h3>6단계 예약 대기 </h3>

예약 대기 API 
- [ ] 예약 대기 요청 기능 
  - 대기 순서가 undefined번째라고 뜨는 문제
- [ ] 예약 대기 취소 기능
  - 삭제할 id를 찾지 못하는 문제 
- [x] 내 예약 목록 조회 시 예약 대기 목록도 함께 포함 
- [x] 중복 예약이 불가능 하도록 구현하세요.