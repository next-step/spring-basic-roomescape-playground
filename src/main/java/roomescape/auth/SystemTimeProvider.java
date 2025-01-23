package roomescape.auth;

import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class SystemTimeProvider implements TimeProvider {
    @Override
    public Date now() {
        return new Date();
    }
}
