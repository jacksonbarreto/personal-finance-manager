package bll.exceptions;

import java.util.ResourceBundle;

public class ExistingFormOfPaymentException extends IllegalArgumentException{
    /**
     * Constructs an {@code IllegalArgumentException} with no
     * detail message.
     */
    public ExistingFormOfPaymentException() {
        super(ResourceBundle.getBundle("lang/errors").getString("existing.formOfPayment"));
    }
}
