package roomescape.domain.member;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.member.web.dto.MemberRequest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberRequestTest {

    private Validator validator;

    private final String name = "Alice";
    private final String email = "dummy@dummy.com";
    private final String password = "dummy";

    @BeforeEach
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void name필드가_null인_MemberRequest를_생성하면_예외를_던진다() {
        // given
        MemberRequest request = new MemberRequest(null, email, password);

        // when
        Set<ConstraintViolation<MemberRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
    }

    @Test
    void name필드가_empty인_MemberRequest를_생성하면_예외를_던진다() {
        // given
        MemberRequest request = new MemberRequest("", email, password);

        // when
        Set<ConstraintViolation<MemberRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
    }

    @Test
    void name필드가_blank인_MemberRequest를_생성하면_예외를_던진다() {
        // given
        MemberRequest request = new MemberRequest(" ", email, password);

        // when
        Set<ConstraintViolation<MemberRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
    }

    @Test
    void email필드가_null인_MemberRequest를_생성하면_예외를_던진다() {
        // given
        MemberRequest request = new MemberRequest(name, null, password);

        // when
        Set<ConstraintViolation<MemberRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
    }

    @Test
    void email필드가_empty인_MemberRequest를_생성하면_예외를_던진다() {
        // given
        MemberRequest request = new MemberRequest(name, "", password);

        // when
        Set<ConstraintViolation<MemberRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
    }

    @Test
    void email필드가_blank인_MemberRequest를_생성하면_예외를_던진다() {
        // given
        MemberRequest request = new MemberRequest(name, " ", password);

        // when
        Set<ConstraintViolation<MemberRequest>> violations = validator.validate(request);

        // then (also not satisfied Email validate)
        assertThat(violations).hasSize(2);
    }

    @Test
    void email필드가_at이_없다면_MemberRequest를_생성할_때_예외를_던진다() {
        // given
        MemberRequest request = new MemberRequest(name, "dummydummy.com", password);

        // when
        Set<ConstraintViolation<MemberRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
    }
}
