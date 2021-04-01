package bll.entities;

import bll.enumerators.EOperationType;
import bll.enumerators.ERepetitionFrequency;
import bll.exceptions.AmountEqualZeroException;
import bll.exceptions.InvalidDescriptionSizeException;
import bll.exceptions.InvalidNameSizeException;
import bll.exceptions.NullArgumentException;
import bll.valueObjects.IAttachment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static bll.enumerators.EOperationType.*;
import static bll.enumerators.ERepetitionFrequency.NONE;

public class Movement extends Operation implements IMovement {

    private EOperationType MovementType;
    private ERepetitionFrequency frequency;
    private UUID groupID;

    public Movement(String name, String description, BigDecimal amount, LocalDate dueDate,
                    IFormOfPayment formOfPayment, IPayee payee, ITransactionCategory category,
                    Set<IAttachment> attachments, EOperationType MovementType,
                    ERepetitionFrequency frequency, UUID groupID) {
        super(name, description, amount, dueDate, formOfPayment, payee, category, attachments);
        if (MovementType == null || frequency == null)
            throw new NullArgumentException();

        if (frequency == NONE){
            if (groupID == null)
                this.groupID = this.ID;
            else
                this.groupID = groupID;
        }else {
            this.groupID = this.ID;
        }

        this.MovementType = MovementType;
        this.frequency = frequency;
        normalizesAmount();
    }


    public Movement(String name, BigDecimal amount, LocalDate dueDate,
                    IFormOfPayment formOfPayment, IPayee payee, ITransactionCategory category,
                    EOperationType MovementType) {
        this(name, EMPTY_DESCRIPTION, amount, dueDate, formOfPayment, payee, category, new HashSet<>(), MovementType, NONE, null);

    }

    public Movement(String name, BigDecimal amount, LocalDate dueDate,
                    IFormOfPayment formOfPayment, IPayee payee, ITransactionCategory category,
                    EOperationType MovementType,
                    ERepetitionFrequency frequency, UUID groupID) {
        this(name, EMPTY_DESCRIPTION, amount, dueDate, formOfPayment, payee, category, new HashSet<>(), MovementType, frequency, groupID);
    }

    public Movement(IMovement movement) {
        if (movement == null)
            throw new NullArgumentException();

        this.name = movement.getName();
        this.description = movement.getDescription();
        this.amount = movement.getAmount();
        this.dueDate = movement.getDueDate();
        this.formOfPayment = movement.getFormOfPayment();
        this.payee = movement.getPayee();
        this.category = movement.getCategory();
        this.ID = movement.getID();
        this.attachments = new HashSet<>();
        this.attachments.addAll(movement.getAttachments());
        this.groupID = movement.getID();
        this.MovementType = movement.isCredit() ? CREDIT : DEBIT;
        this.frequency = movement.getRepetitionFrequency();
    }


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
    @Override
    public void updateAmount(BigDecimal newAmount) {
        if (newAmount == null)
            throw new NullArgumentException();
        this.amount = newAmount;
        normalizesAmount();
    }

    /**
     * Updates the movement of the due date.
     *
     * @param newDueDate new expiration date.
     * @throws NullArgumentException if the argument is null.     *
     */
    @Override
    public void updateDueDate(LocalDate newDueDate) {
        if (newDueDate == null)
            throw new NullArgumentException();
        this.dueDate = newDueDate;
    }

    /**
     * Allows you to change the name of the Movement.
     *
     * @param newName the new name of the Movement.
     * @throws InvalidNameSizeException if the size of the name attribute
     *                                  does not correspond to the minimum and / or maximum limits
     *                                  defined in an MINIMUM_NAME_SIZE and MAXIMUM_NAME_SIZE.
     * @throws NullArgumentException    if the argument is null.
     */
    @Override
    public void updateName(String newName) {
        if (newName == null)
            throw new NullArgumentException();
        if (INCORRECT_NAME_SIZE.test(newName))
            throw new InvalidNameSizeException();
        this.name = newName;
    }

    /**
     * Updates the description of the movement.
     *
     * @param newDescription new description of the movement.
     * @throws InvalidDescriptionSizeException if the size of the description attribute
     *                                         does not correspond to the minimum and / or maximum limits
     *                                         defined in an MINIMUM_DESCRIPTION_SIZE and MAXIMUM_DESCRIPTION_SIZE.
     * @throws NullArgumentException           if the argument is null.
     */
    @Override
    public void updateDescription(String newDescription) {
        if (newDescription == null)
            throw new NullArgumentException();
        if (INCORRECT_DESCRIPTION_SIZE.test(newDescription))
            throw new InvalidNameSizeException();

        this.description = newDescription;
    }

    /**
     * Updates the movement category ({@code ITransactionCategory}).
     *
     * @param newCategory new category of the movement.
     * @throws NullArgumentException if the argument is null.
     */
    @Override
    public void updateCategory(ITransactionCategory newCategory) {
        if (newCategory == null)
            throw new NullArgumentException();
        this.category = newCategory;
    }

    /**
     * Updates the {@code IPayee} of the movement.
     *
     * @param newPayee new payee of the movement.
     * @throws NullArgumentException if the argument is null.
     */
    @Override
    public void updatePayee(IPayee newPayee) {
        if (newPayee == null)
            throw new NullArgumentException();
        this.payee = newPayee;
    }

    /**
     * Updates the {@code EOperationType} of the movement.
     *
     * @param type new type of the movement.
     * @throws NullArgumentException if the argument is null.
     */
    @Override
    public void updateMovementType(EOperationType type) {
        if (type == null)
            throw new NullArgumentException();
        this.MovementType = type;
        normalizesAmount();
    }

    /**
     * Updates the {@code IFormOfPayment} of the movement.
     *
     * @param newFormOfPayment new Form Of Payment of the movement.
     * @throws NullArgumentException if the argument is null.
     */
    @Override
    public void updateFormOfPayment(IFormOfPayment newFormOfPayment) {
        if (newFormOfPayment == null)
            throw new NullArgumentException();
        this.formOfPayment = newFormOfPayment;
    }

    /**
     * Updates the frequency of repetition of the movement.
     *
     * @param newFrequency the new frequency of repetition of the movement.
     */
    @Override
    public void updateRepetitionFrequency(ERepetitionFrequency newFrequency) {
        if (newFrequency == null)
            throw new NullArgumentException();
        this.frequency = newFrequency;
    }

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
    @Override
    public UUID getGroupID() {
        return this.groupID;
    }

    /**
     * Returns the frequency of repetition of the movement.
     *
     * @return the frequency of repetition of the movement.
     */
    @Override
    public ERepetitionFrequency getRepetitionFrequency() {
        return this.frequency;
    }


    /**
     * Returns if it is a recurring movement.
     * <p>
     * If a movement is a recurrence, then it cannot be an installment or a common movement.
     *
     * @return {@code true} if it is a recurring movement.
     */
    @Override
    public boolean isRecurrent() {
        return this.frequency != NONE && groupID.equals(ID);
    }

    /**
     * Returns if it is a installment movement.
     * <p>
     * If a movement is an installment, then it cannot be a recurrence or a common movement.
     *
     * @return {@code true} if it is a installment movement.
     */
    @Override
    public boolean isInstallment() {
        return this.frequency == NONE && !groupID.equals(ID);
    }

    /**
     * Returns if it is a common movement.
     * <p>
     * If a movement is common it cannot be recurrent or in installments.
     *
     * @return {@code true} if it is a common movement.
     */
    @Override
    public boolean isCommonMovement() {
        return this.frequency == NONE && groupID.equals(ID);
    }

    /**
     * Indicates whether some other Movement is "equal to" this one.
     * <p>
     * It is only the same when both Movement have all their attributes equal.
     *
     * @param o an object.
     * @return {@code true} if only the same when both objects have all their attributes equal.
     */
    @Override
    public boolean isDeepEquals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.isDeepEquals(o)) return false;
        Movement movement = (Movement) o;
        return MovementType == movement.MovementType && frequency == movement.frequency && groupID.equals(movement.groupID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), MovementType, frequency, groupID);
    }

    /**
     * Returns a clone of the current object.
     *
     * @return a clone of the current instance.
     */
    @Override
    public IMovement clone() {
        return new Movement(this);
    }

    @Override
    public String toString() {
        return "Movement{" +
                "MovementType=" + MovementType +
                ", frequency=" + frequency +
                ", groupID=" + groupID +
                ", ID=" + ID +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", dueDate=" + dueDate +
                ", formOfPayment=" + formOfPayment +
                ", payee=" + payee +
                ", category=" + category +
                ", attachments=" + attachments +
                '}';
    }

    @SuppressWarnings("unused")
    private Movement() {
    }

    @SuppressWarnings("unused")
    private void setID(UUID ID) {
        this.ID = ID;
    }

    @SuppressWarnings("unused")
    private void setGroupID(UUID groupID) {
        this.groupID = groupID;
    }

    @SuppressWarnings("unused")
    private void setName(String name) {
        this.name = name;
    }

    @SuppressWarnings("unused")
    private void setDescription(String description) {
        this.description = description;
    }

    @SuppressWarnings("unused")
    private void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @SuppressWarnings("unused")
    private void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    @SuppressWarnings("unused")
    private void setFormOfPayment(IFormOfPayment formOfPayment) {
        this.formOfPayment = formOfPayment;
    }

    @SuppressWarnings("unused")
    private void setPayee(IPayee payee) {
        this.payee = payee;
    }

    @SuppressWarnings("unused")
    private void setCategory(ITransactionCategory category) {
        this.category = category;
    }

    @SuppressWarnings("unused")
    private void setAttachments(Set<IAttachment> attachments) {
        this.attachments = attachments;
    }

    @SuppressWarnings("unused")
    private void setMovementType(EOperationType movementType) {
        this.MovementType = movementType;
    }

    @SuppressWarnings("unused")
    private void setFrequency(ERepetitionFrequency frequency) {
        this.frequency = frequency;
    }

    private void normalizesAmount() {
        if (this.MovementType == CREDIT && this.amount.compareTo(BigDecimal.ZERO) < 0) {
            this.amount = this.amount.multiply(new BigDecimal("-1"));
        } else if (this.MovementType == DEBIT && this.amount.compareTo(BigDecimal.ZERO) > 0) {
            this.amount = this.amount.multiply(new BigDecimal("-1"));
        }
    }
}
