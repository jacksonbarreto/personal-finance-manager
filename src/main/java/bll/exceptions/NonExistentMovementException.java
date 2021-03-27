package bll.exceptions;

public class NonExistentMovementException extends IllegalArgumentException{
    /**
     * Constructs an {@code IllegalArgumentException} with no
     * detail message.
     */
    public NonExistentMovementException() {
        super();
    }
}
