package roomescape.auth;

import java.util.Date;

import roomescape.auth.util.TimeProvider;

public class TestTimeProvider implements TimeProvider {

	private Date fixedDate;

	public TestTimeProvider(Date fixedDate) {
		this.fixedDate = fixedDate;
	}

	@Override
	public Date now() {
		return fixedDate;
	}


	public void decreaseTime(long milliseconds) {
		this.fixedDate = new Date(fixedDate.getTime() - milliseconds);
	}
}
