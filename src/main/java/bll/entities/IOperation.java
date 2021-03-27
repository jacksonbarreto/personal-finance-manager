package bll.entities;

import bll.exceptions.AttachmentDoesNotExistException;
import bll.exceptions.NullArgumentException;
import bll.valueObjects.IAttachment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public interface IOperation extends Serializable, Comparable<IOperation> {
    int MINIMUM_NAME_SIZE = 3;
    int MAXIMUM_NAME_SIZE = 30;
    int MINIMUM_DESCRIPTION_SIZE = 3;
    int MAXIMUM_DESCRIPTION_SIZE = 250;
    String EMPTY_DESCRIPTION = "";
    Predicate<BigDecimal> AMOUNT_IS_ZERO = (amount) -> amount.equals(BigDecimal.ZERO);
    Predicate<String> INCORRECT_NAME_SIZE = (s) -> (s.length() < MINIMUM_NAME_SIZE || s.length() > MAXIMUM_NAME_SIZE);
    Predicate<String> INCORRECT_DESCRIPTION_SIZE = (s) -> !s.isEmpty() && (s.length() < MINIMUM_DESCRIPTION_SIZE || s.length() > MAXIMUM_DESCRIPTION_SIZE);
    Comparator<IOperation> COMPARE_FOR_AMOUNT = (op1,op2)-> op1.getAmount().compareTo(op2.getAmount());
    /**
     * Returns the unique identifier of the Operation.
     *
     * @return the unique identifier of the Operation.
     */
    UUID getID();

    /**
     * Returns the name of the Operation.
     *
     * @return the name of the Operation.
     */
    String getName();

    /**
     * Returns the monetary value of the Operation.
     *
     * @return the monetary value of the Operation.
     */
    BigDecimal getAmount();

    /**
     * Returns the due date of the Operation.
     *
     * @return the due date of the Operation.
     */
    LocalDate getDueDate();

    /**
     * Returns the reference (month / year) of the Operation.
     *
     * @return the reference (month / year) of the Operation.
     */
    YearMonth getReference();

    /**
     * Returns the description of the Operation.
     *
     * @return the description of the Operation.
     */
    String getDescription();

    /**
     * Returns the payee of the Operation.
     *
     * @return the payee of the Operation.
     */
    IPayee getPayee();

    /**
     * Returns the category of the Operation.
     *
     * @return the category of the Operation.
     */
    ITransactionCategory getCategory();

    /**
     * Returns the payment method of the Operation.
     *
     * @return the payment method of the Operation.
     */
    IFormOfPayment getFormOfPayment();

    /**
     * Returns a collection of Operation attachments.
     *
     * @return a collection of Operation attachments.
     */
    Set<IAttachment> getAttachments();

    /**
     * Adds an attachment to the operation.
     *
     * @param newAttachment an attachment.
     * @throws NullArgumentException if the argument is null.
     */
    void addAttachment(IAttachment newAttachment);

    /**
     * Remove an attachment to the operation.
     *
     * @param attachment an existing attachment in the operation.
     * @throws NullArgumentException           if the argument is null.
     * @throws AttachmentDoesNotExistException if the attachment does not exist in the operation.
     */
    void removeAttachment(IAttachment attachment);

    /**
     * Indicates whether some other Movement is "equal to" this one.
     * <p>
     * It is only the same when both objects have ID equal.
     *
     * @param o an instance of Object.
     * @return {@code true} only if both objects have the same ID.
     */
    @Override
    boolean equals(Object o);

    /**
     * Indicates whether some other Movement is "equal to" this one.
     * <p>
     * It is only the same when both Movement have all their attributes equal.
     *
     * @param o an object.
     * @return {@code true} if only the same when both objects have all their attributes equal.
     */
    boolean isDeepEquals(Object o);

    /**
     * Returns if the movement is of the debit type.
     * A debit is a operation less that zero.
     *
     * @return {@code true} if the movement is of the debit type.
     */
    boolean isDebit();

    /**
     * Returns if the movement is of the credit type.
     * A credit is a operation greater that zero.
     *
     * @return {@code true} if the movement is of the credit type.
     */
    boolean isCredit();

    /**
     * Returns a string representation of the email object.
     *
     * @return a string representation of the email object.
     */
    String toString();
}
