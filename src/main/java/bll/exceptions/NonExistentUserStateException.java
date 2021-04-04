package bll.exceptions;

public class NonExistentUserStateException extends IllegalArgumentException{
    /**
     * Constructs an {@code IllegalArgumentException} with no
     * detail message.
     */
    public NonExistentUserStateException() {
        super();
    }
}
