package roomescape.waiting.domain;

public enum Status {

    CONFIRMED("예약"),
    WAITING("번째 예약대기");

    private String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus(long rank) {
        if (this == WAITING) {
            return rank + status;
        }
        return status;
    }

    public String getStatus() {
        return status;
    }
}
