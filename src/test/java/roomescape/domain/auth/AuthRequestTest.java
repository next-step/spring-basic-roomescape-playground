package roomescape.domain.auth;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.auth.web.dto.AuthRequest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthRequestTest {

    private Validator validator;

    private String email = "dummy@dummy.com";
    private String password = "password";

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void email필드가_null인_AuthRequest를_생성하면_예외를_던진다() {
        // given
        AuthRequest emailIsNull = new AuthRequest(
                null, password
        );

        // when
        Set<ConstraintViolation<AuthRequest>> violations = validator.validate(emailIsNull);

        // then
        assertThat(violations).hasSize(1);
    }

    @Test
    void email필드가_비어_있는_AuthRequest를_생성하면_예외를_던진다() {
        // given
        AuthRequest emailIsNull = new AuthRequest(
                "", password
        );

        // when
        Set<ConstraintViolation<AuthRequest>> violations = validator.validate(emailIsNull);

        // then
        assertThat(violations).hasSize(1);
    }

    @Test
    void email필드가_공백인_AuthRequest를_생성하면_예외를_던진다() {
        // given
        AuthRequest emailIsNull = new AuthRequest(
                " ", password
        );

        // when
        Set<ConstraintViolation<AuthRequest>> violations = validator.validate(emailIsNull);

        // then
        assertThat(violations).hasSize(1);
    }

    @Test
    void password필드가_null인_AuthRequest를_생성하면_예외를_던진다() {
        // given
        AuthRequest emailIsNull = new AuthRequest(
                email, null
        );

        // when
        Set<ConstraintViolation<AuthRequest>> violations = validator.validate(emailIsNull);

        // then
        assertThat(violations).hasSize(1);
    }

    @Test
    void password필드가_비어_있는_AuthRequest를_생성하면_예외를_던진다() {
        // given
        AuthRequest emailIsNull = new AuthRequest(
                email, ""
        );

        // when
        Set<ConstraintViolation<AuthRequest>> violations = validator.validate(emailIsNull);

        // then
        assertThat(violations).hasSize(1);
    }

    @Test
    void password필드가_공백인_AuthRequest를_생성하면_예외를_던진다() {
        // given
        AuthRequest emailIsNull = new AuthRequest(
                email, " "
        );

        // when
        Set<ConstraintViolation<AuthRequest>> violations = validator.validate(emailIsNull);

        // then
        assertThat(violations).hasSize(1);
    }
}
