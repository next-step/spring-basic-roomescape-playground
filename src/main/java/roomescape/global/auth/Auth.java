package roomescape.global.auth;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(PARAMETER)
@Retention(RUNTIME)
public @interface Auth {
}
