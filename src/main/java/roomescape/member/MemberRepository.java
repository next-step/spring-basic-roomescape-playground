package roomescape.member;


import org.springframework.data.repository.CrudRepository;
//MemberRepository는 SpringDataJPA를 사용하여 Member 엔터티에 대한 데이터 접근을 처리하는 인터페이스
//CrudRepostiory를 확장하여 기본적인 CRUD기능 제공

public interface MemberRepository extends CrudRepository<Member, Long> {
    //Member 엔터티와 그 ID 타입이 long
    Member findByName(String name);
    //주어진 이름을 가진 Member엔터티 검색
    Member findByEmailAndPassword(String email, String password);
    //이메일과 비밀번호를 가진 Member엔터티를 검색
    //이메일과 비밀번호가 일치하는 멤버를 반환
    boolean existsByEmailAndPassword(String email, String password);
    //주어진 이메일과 비밀번호를가진 Member 엔터티가 존재하는지 여부를 확인
    //존재하면 true 존재하지 않으면 false 반환
}

//MemberRepository인터페이스는 Member엔터티에 대한 데이터 접근을 캡슐화하여 데이터 베읏와의 상호작용을 쉽게 처리할 수 있게 한다.
//Spring Data JPA의 메서드 이름 규칙을 통해 복잡한 쿼리 작성 없이도 간단한 데이터베이스 조회 기능 구현

