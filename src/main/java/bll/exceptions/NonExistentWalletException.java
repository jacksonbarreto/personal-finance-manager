package bll.exceptions;

public class NonExistentWalletException extends IllegalArgumentException{
    /**
     * Constructs an {@code IllegalArgumentException} with no
     * detail message.
     */
    public NonExistentWalletException() {
        super();
    }
}
