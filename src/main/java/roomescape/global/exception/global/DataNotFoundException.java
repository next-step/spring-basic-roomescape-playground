package roomescape.global.exception.global;

public abstract class DataNotFoundException extends RuntimeException{

    protected DataNotFoundException(String message) {
        super(message);
    }
}
