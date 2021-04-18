package bll.exceptions;

public class SessionAlreadyHasUserException extends IllegalArgumentException{
    /**
     * Constructs an {@code IllegalArgumentException} with no
     * detail message.
     */
    public SessionAlreadyHasUserException() {
        super();
    }
}
