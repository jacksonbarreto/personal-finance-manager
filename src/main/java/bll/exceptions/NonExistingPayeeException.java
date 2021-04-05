package bll.exceptions;

public class NonExistingPayeeException extends IllegalArgumentException{
    /**
     * Constructs an {@code IllegalArgumentException} with no
     * detail message.
     */
    public NonExistingPayeeException() {
        super();
    }
}
