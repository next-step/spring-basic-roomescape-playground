package roomescape.domain.theme;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.theme.web.dto.ThemeRequest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ThemeRequestTest {

    private Validator validator;

    private String name = "Dummy";
    private String description = "dummy description for test";

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void name필드가_null인_ThemeRequest를_생성하면_예외를_던진다() {
        // then
        assertThat(
                hasViolateInRequest(
                        // given
                        new ThemeRequest(null, description)
                )
        ).isTrue();
    }

    @Test
    void name필드가_empty인_ThemeRequest를_생성하면_예외를_던진다() {
        // then
        assertThat(
                hasViolateInRequest(
                        // given
                        new ThemeRequest("", description)
                )
        ).isTrue();
    }

    @Test
    void name필드가_blank인_ThemeRequest를_생성하면_예외를_던진다() {
        // then
        assertThat(
                hasViolateInRequest(
                        // given
                        new ThemeRequest(" ", description)
                )
        ).isTrue();
    }

    @Test
    void description필드가_null인_ThemeRequest를_생성하면_예외를_던진다() {
        // then
        assertThat(
                hasViolateInRequest(
                        // given
                        new ThemeRequest(name, null)
                )
        ).isTrue();
    }

    @Test
    void description필드가_empty인_ThemeRequest를_생성하면_예외를_던진다() {
        // then
        assertThat(
                hasViolateInRequest(
                        // given
                        new ThemeRequest(name, "")
                )
        ).isTrue();
    }

    @Test
    void description필드가_blank인_ThemeRequest를_생성하면_예외를_던진다() {
        // then
        assertThat(
                hasViolateInRequest(
                        // given
                        new ThemeRequest(name, " ")
                )
        ).isTrue();
    }

    // when
    private boolean hasViolateInRequest(ThemeRequest request) {
        Set<ConstraintViolation<ThemeRequest>> violations = validator.validate(request);

        return !violations.isEmpty();
    }
}
