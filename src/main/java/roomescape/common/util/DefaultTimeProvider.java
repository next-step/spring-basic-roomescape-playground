package roomescape.common.util;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class DefaultTimeProvider implements TimeProvider {
	@Override
	public Date now() {
		return new Date();
	}
}
