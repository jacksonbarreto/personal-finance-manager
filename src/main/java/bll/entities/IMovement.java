package bll.entities;

import bll.enumerators.EOperationType;
import bll.enumerators.ERepetitionFrequency;
import bll.exceptions.*;
import bll.valueObjects.IAttachment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public interface IMovement extends Serializable, Comparable<IMovement>, Cloneable {
    int MINIMUM_NAME_SIZE = 3;
    int MAXIMUM_NAME_SIZE = 30;
    int MINIMUM_DESCRIPTION_SIZE = 3;
    int MAXIMUM_DESCRIPTION_SIZE = 250;
    String EMPTY_DESCRIPTION = "";
    Predicate<BigDecimal> AMOUNT_IS_ZERO = (amount) -> amount.equals(BigDecimal.ZERO);
    Predicate<String> INCORRECT_NAME_SIZE = (s) -> (s.length() < MINIMUM_NAME_SIZE || s.length() > MAXIMUM_NAME_SIZE);
    Predicate<String> INCORRECT_DESCRIPTION_SIZE = (s) -> !s.isEmpty() && (s.length() < MINIMUM_DESCRIPTION_SIZE || s.length() > MAXIMUM_DESCRIPTION_SIZE);
    Comparator<IMovement> COMPARE_FOR_AMOUNT = Comparator.comparing(IMovement::getAmount);
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
     * Returns the accomplish date.
     *
     * It may be {@code null} if the movement is not yet accomplished.
     *
     * @return the accomplish date.
     */
    LocalDate getAccomplishDate();

    /**
     * Returns the date of registration of the movement.
     *
     * @return the date of registration of the movement.
     */
    LocalDate getRegistrationDate();


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
    IMovementCategory getCategory();

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
     * Inactivates a movement, it is the equivalent of deleting a movement.
     */
    void inactivate();

    /**
     * Returns if {@code true} if a movement is active, that is, not deleted.
     *
     * @return if {@code true} if a movement is active, that is, not deleted.
     */
    boolean isActive();

    /**
     * Returns if {@code true} if a movement is inactive, that is, deleted.
     *
     * @return if {@code true} if a movement is inactive, that is, deleted.
     */
    boolean isInactive();

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
     * Updates the movement value.
     * The sign of the value (positive or negative) is indifferent,
     * as the sign is automatically defined, depending on
     * the type of operation (debit or credit).
     *
     * @param newAmount movement value.
     * @throws NullArgumentException    if the argument is null.
     * @throws AmountEqualZeroException if the amount is equals zero.
     */
    void updateAmount(BigDecimal newAmount);

    /**
     * Updates the movement of the due date.
     *
     * @param newDueDate new expiration date.
     * @throws NullArgumentException if the argument is null.     *
     */
    void updateDueDate(LocalDate newDueDate);

    /**
     * Allows you to change the name of the Movement.
     *
     * @param newName the new name of the Movement.
     * @throws InvalidNameSizeException if the size of the name attribute
     *                                  does not correspond to the minimum and / or maximum limits
     *                                  defined in an MINIMUM_NAME_SIZE and MAXIMUM_NAME_SIZE.
     * @throws NullArgumentException    if the argument is null.
     */
    void updateName(String newName);

    /**
     * Updates the description of the movement.
     *
     * @param newDescription new description of the movement.
     * @throws InvalidDescriptionSizeException if the size of the description attribute
     *                                         does not correspond to the minimum and / or maximum limits
     *                                         defined in an MINIMUM_DESCRIPTION_SIZE and MAXIMUM_DESCRIPTION_SIZE.
     * @throws NullArgumentException           if the argument is null.
     */
    void updateDescription(String newDescription);

    /**
     * Updates the movement category ({@code ITransactionCategory}).
     *
     * @param newCategory new category of the movement.
     * @throws NullArgumentException if the argument is null.
     */
    void updateCategory(IMovementCategory newCategory);

    /**
     * Updates the {@code IPayee} of the movement.
     *
     * @param newPayee new payee of the movement.
     * @throws NullArgumentException if the argument is null.
     */
    void updatePayee(IPayee newPayee);

    /**
     * Updates the {@code EOperationType} of the movement.
     *
     * @param type new type of the movement.
     * @throws NullArgumentException if the argument is null.
     */
    void updateMovementType(EOperationType type);


    /**
     * Updates the {@code IFormOfPayment} of the movement.
     *
     * @param newFormOfPayment new Form Of Payment of the movement.
     * @throws NullArgumentException if the argument is null.
     */
    void updateFormOfPayment(IFormOfPayment newFormOfPayment);

    /**
     * Updates the frequency of repetition of the movement.
     *
     * @param newFrequency the new frequency of repetition of the movement.
     */
    void updateRepetitionFrequency(ERepetitionFrequency newFrequency);

    /**
     * Returns the group identifier for the movement.
     * <p>
     * The group identifier identifies a group of movements
     * that belong to the same installment or recurrence.
     * <p>
     * In the case of a simple movement, it returns {@code null}.
     *
     * @return the group identifier for the movement OR {@code null} In the case of a simple movement.
     */
    UUID getGroupID();

    /**
     * Returns the frequency of repetition of the movement.
     *
     * @return the frequency of repetition of the movement.
     */
    ERepetitionFrequency getRepetitionFrequency();

    /**
     * Makes the movement accomplished in current date.
     */
    void accomplish();

    /**
     * Makes the movement accomplished.
     *
     * @param accomplishDate from occurrence.
     * @throws NullArgumentException if the argument is null.
     */
    void accomplish(LocalDate accomplishDate);


    /**
     * Returns whether the movement was accomplished.
     *
     * @return {@code true} whether the movement was accomplished.
     */
    boolean isAccomplished();


    /**
     * Returns if it is a recurring movement.
     * <p>
     * If a movement is a recurrence, then it cannot be an installment or a common movement.
     *
     * @return {@code true} if it is a recurring movement.
     */
    boolean isRecurrent();

    /**
     * Returns if it is a installment movement.
     * <p>
     * If a movement is an installment, then it cannot be a recurrence or a common movement.
     *
     * @return {@code true} if it is a installment movement.
     */
    boolean isInstallment();

    /**
     * Returns if it is a common movement.
     * <p>
     * If a movement is common it cannot be recurrent or in installments.
     *
     * @return {@code true} if it is a common movement.
     */
    boolean isCommonMovement();

    /**
     * Returns a clone of the current object.
     *
     * @return a clone of the current instance.
     */
    IMovement clone();


    /**
     * Returns a string representation of the email object.
     *
     * @return a string representation of the email object.
     */
    String toString();
}
