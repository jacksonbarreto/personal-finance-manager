package bll.exceptions;

public class AttemptedToUseExcludedMovementException extends IllegalArgumentException{
    /**
     * Constructs an {@code IllegalArgumentException} with no
     * detail message.
     */
    public AttemptedToUseExcludedMovementException() {
        super();
    }
}
