package roomescape;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class QueryTryTest {

    @Autowired
    private TestMemberRepository memberRepository;

    private Long memberId;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setup() {
        // [1] 초기 데이터 저장 (SELECT/DELETE 쿼리 수에 포함되지 않음)
        TestMember member = new TestMember("testUser");
        TestMember savedMember = memberRepository.save(member);
        memberId = savedMember.getId();

        // testUser를 영속성 컨텍스트에서 비우기 위함
        entityManager.flush();
    }


    @Test
    void deleteById_호출시_쿼리_확인() {
        System.out.println("--- deleteById() 시작 ---");

        // 1. deleteById 호출
        memberRepository.deleteById(memberId);

        memberRepository.flush();

        // 2. 쿼리 실행 후, 다음 코드가 실행되는지 확인하기 위해 임시로 출력
        System.out.println("--- DELETE 쿼리 실행 완료 ---");

        // 3. 삭제 확인
        assertThat(memberRepository.findById(memberId)).isEmpty();

        System.out.println("--- 테스트 메서드 끝 ---");

    }

}


@Entity
class TestMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public TestMember(String name) {
        this.name = name;
    }

    public TestMember() {

    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

@Repository
interface TestMemberRepository extends JpaRepository<TestMember, Long> {

}
