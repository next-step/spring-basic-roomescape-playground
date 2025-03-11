package roomescape;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import roomescape.member.MemberController;
import roomescape.member.MemberRequest;
import roomescape.member.MemberResponse;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberController memberController;

    @Test
    void createMemberTest() {
        // given
        MemberRequest memberRequest = new MemberRequest("Doyo","member@example.com", "password");

        // when
        ResponseEntity<MemberResponse> response = memberController.createMember(memberRequest);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Doyo");
        assertThat(response.getBody().getEmail()).isEqualTo("member@example.com");
    }
}
