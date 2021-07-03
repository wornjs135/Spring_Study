package inu.appcenter.study4.exception;

public class AuthenticationException extends RuntimeException{
    public AuthenticationException() {
        super();
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(Throwable cause) {
        super(cause);
    }
}
