package roomescape;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:readme-requirement-test")
@AutoConfigureMockMvc
class AuthenticationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void loginReturnsTokenCookie() throws Exception {
        // given
        String requestBody = loginRequest("admin@email.com", "password");

        // when
        MvcResult result = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andReturn();

        // then
        Cookie tokenCookie = result.getResponse().getCookie("token");
        assertEquals(200, result.getResponse().getStatus());
        assertNotNull(tokenCookie);
        assertFalse(tokenCookie.getValue().isBlank());
    }

    @Test
    void loginCheckReturnsMemberInfoFromCookie() throws Exception {
        // given
        Cookie tokenCookie = loginAndExtractToken("brown@email.com", "password");

        // when
        MvcResult result = mockMvc.perform(get("/login/check")
                        .cookie(tokenCookie))
                .andReturn();

        // then
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("brown@email.com", jsonValue(result, "email"));
        assertEquals("USER", jsonValue(result, "role"));
    }

    @Test
    void createReservationUsesLoginMemberNameWhenNameIsMissing() throws Exception {
        // given
        Cookie tokenCookie = loginAndExtractToken("admin@email.com", "password");
        MvcResult loginCheckResult = mockMvc.perform(get("/login/check")
                        .cookie(tokenCookie))
                .andReturn();
        String loginMemberName = jsonValue(loginCheckResult, "name");
        String requestBody = """
                {
                    "date": "2024-03-02",
                    "theme": 1,
                    "time": 1
                }
                """;

        // when
        MvcResult result = mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(tokenCookie)
                        .content(requestBody))
                .andReturn();

        // then
        assertEquals(201, result.getResponse().getStatus());
        assertEquals(loginMemberName, jsonValue(result, "name"));
    }

    @Test
    void onlyAdminCanAccessAdminPage() throws Exception {
        // given
        Cookie userTokenCookie = loginAndExtractToken("brown@email.com", "password");
        Cookie adminTokenCookie = loginAndExtractToken("admin@email.com", "password");

        // when
        MvcResult userResult = mockMvc.perform(get("/admin")
                        .cookie(userTokenCookie))
                .andReturn();
        MvcResult adminResult = mockMvc.perform(get("/admin")
                        .cookie(adminTokenCookie))
                .andReturn();

        // then
        assertEquals(401, userResult.getResponse().getStatus());
        assertEquals(200, adminResult.getResponse().getStatus());
    }

    private Cookie loginAndExtractToken(String email, String password) throws Exception {
        MvcResult result = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest(email, password)))
                .andReturn();

        return result.getResponse().getCookie("token");
    }

    private String loginRequest(String email, String password) {
        return """
                {
                    "email": "%s",
                    "password": "%s"
                }
                """.formatted(email, password);
    }

    private String jsonValue(MvcResult result, String fieldName) throws Exception {
        return objectMapper.readTree(result.getResponse().getContentAsString())
                .get(fieldName)
                .asText();
    }
}