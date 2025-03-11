package roomescape;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.test.annotation.DirtiesContext;
import roomescape.member.MemberController;
import roomescape.member.dto.request.MemberRequest;
import roomescape.member.MemberResponse;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MemberControllerTest {

    @Autowired
    private MemberController memberController;

    @Test
    @DisplayName("아이디_생성_테스트")
    void createMemberTest() {
        // given
        MemberRequest memberRequest = new MemberRequest("Doyo","member@example.com", "password");

        // when
        ResponseEntity<MemberResponse> response = memberController.createMember(memberRequest);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        MemberResponse responseBody = response.getBody();
        assertThat(responseBody.getId()).isNotNull();
        assertThat(responseBody.getName()).isEqualTo("Doyo");
        assertThat(responseBody.getEmail()).isEqualTo("member@example.com");
    }
}
