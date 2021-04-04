package bll.exceptions;

public class ExistingPayeeException extends IllegalArgumentException{
    /**
     * Constructs an {@code IllegalArgumentException} with no
     * detail message.
     */
    public ExistingPayeeException() {
        super();
    }
}
