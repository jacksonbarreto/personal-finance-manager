package bll.exceptions;

/**
 * Launched to indicate that the size of the name attribute does not correspond
 * to the minimum and / or maximum limits defined in an implementation.
 */
public class InvalidNameSizeException extends IllegalArgumentException {
    public InvalidNameSizeException() {
        super();
    }
}
