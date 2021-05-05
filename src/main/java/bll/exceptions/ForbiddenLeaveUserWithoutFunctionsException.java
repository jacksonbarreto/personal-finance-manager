package bll.exceptions;

import java.util.ResourceBundle;

public class ForbiddenLeaveUserWithoutFunctionsException extends IllegalArgumentException{
    /**
     * Constructs an {@code IllegalArgumentException} with no
     * detail message.
     */
    public ForbiddenLeaveUserWithoutFunctionsException() {
        super(ResourceBundle.getBundle("lang/errors").getString("without.role"));
    }
}
