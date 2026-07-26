package roomescape.fixture;

import roomescape.time.entity.Time;

@SuppressWarnings("NonAsciiCharacters")
public class TimeFixture {

    public static Time 시간_시간1_생성() {
        return Time.from("10:00");
    }

    public static Time 시간_시간2_생성() {
        return Time.from("12:00");
    }

    public static Time 시간_시간3_생성() {
        return Time.from("14:00");
    }
}
