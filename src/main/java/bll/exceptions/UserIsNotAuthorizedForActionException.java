package bll.exceptions;

public class UserIsNotAuthorizedForActionException extends IllegalArgumentException{
    /**
     * Constructs an {@code IllegalArgumentException} with no
     * detail message.
     */
    public UserIsNotAuthorizedForActionException() {
        super();
    }
}
