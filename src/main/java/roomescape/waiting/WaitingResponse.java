package roomescape.waiting;

public class WaitingResponse {
    private Long id;

    public WaitingResponse() {}

    public WaitingResponse(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
