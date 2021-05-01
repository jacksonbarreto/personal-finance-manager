package bll.exceptions;

import java.util.ResourceBundle;

public class ExistingCategoryException extends IllegalArgumentException{
    /**
     * Constructs an {@code IllegalArgumentException} with no
     * detail message.
     */
    public ExistingCategoryException() {
        super(ResourceBundle.getBundle("lang/errors").getString("existing.category"));
    }
}
