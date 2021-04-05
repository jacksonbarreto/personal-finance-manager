package bll.exceptions;

public class NonExistingCategoryException extends IllegalArgumentException{
    /**
     * Constructs an {@code IllegalArgumentException} with no
     * detail message.
     */
    public NonExistingCategoryException() {
        super();
    }
}
