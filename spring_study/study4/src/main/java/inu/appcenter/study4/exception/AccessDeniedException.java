package inu.appcenter.study4.exception;

public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException() {
        super();
    }

    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(Throwable cause) {
        super(cause);
    }
}
