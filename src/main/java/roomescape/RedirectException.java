package roomescape;

import java.net.URI;
import org.springframework.http.HttpStatusCode;

public class RedirectException extends RuntimeException {
    private final HttpStatusCode status;
    private final URI location;

    public RedirectException(HttpStatusCode status, URI location) {
        super();
        this.status = status;
        this.location = location;
    }

    public HttpStatusCode getStatus() {
        return status;
    }

    public URI getLocation() {
        return location;
    }
}
