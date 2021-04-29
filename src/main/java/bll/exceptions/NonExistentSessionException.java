package bll.exceptions;

public class NonExistentSessionException extends IllegalArgumentException{
    /**
     * Constructs an {@code IllegalArgumentException} with no
     * detail message.
     */
    public NonExistentSessionException() {
        super();
    }
}
