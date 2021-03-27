package bll.entities;

import bll.enumerators.EOperationType;
import bll.enumerators.ERepetitionFrequency;
import bll.exceptions.AmountEqualZeroException;
import bll.exceptions.InvalidDescriptionSizeException;
import bll.exceptions.InvalidNameSizeException;
import bll.exceptions.NullArgumentException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public interface IMovement extends IOperation {


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
    void updateCategory(ITransactionCategory newCategory);

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
     * Returns if it is a recurring movement.
     * <p>
     * A movement can not be recurring and installment at the same time.
     *
     * @return {@code true} if it is a recurring movement.
     */
    boolean isRecurrent();

    /**
     * Returns a clone of the current object.
     *
     * @return a clone of the current instance.
     */
    IMovement clone();
}
