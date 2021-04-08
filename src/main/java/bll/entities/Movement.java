package bll.entities;

import bll.enumerators.EOperationType;
import bll.enumerators.ERepetitionFrequency;
import bll.exceptions.*;
import bll.valueObjects.IAttachment;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

import static bll.enumerators.EOperationType.*;
import static bll.enumerators.ERepetitionFrequency.NONE;

@Entity
public class Movement implements IMovement {

    @Id
    private UUID ID;
    @Column(nullable = false, length = MAXIMUM_NAME_SIZE)
    private String name;
    @Column(nullable = false, length = MAXIMUM_DESCRIPTION_SIZE)
    private String description;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(nullable = false)
    private LocalDate dueDate;
    @JoinColumn(nullable = false)
    @OneToOne(targetEntity = FormOfPayment.class)
    private IFormOfPayment formOfPayment;
    @JoinColumn(nullable = false)
    @OneToOne(targetEntity = Payee.class)
    private IPayee payee;
    @JoinColumn(nullable = false)
    @OneToOne(targetEntity = MovementCategory.class)
    private IMovementCategory category;
    @ElementCollection
    private Set<IAttachment> attachments;
    @Column(nullable = false)
    @Enumerated
    private EOperationType movementType;
    @Column(nullable = false)
    @Enumerated
    private ERepetitionFrequency frequency;
    @Column(nullable = false)
    private UUID groupID;
    private boolean accomplished;
    private LocalDate accomplishDate;
    @Column(nullable = false)
    private LocalDate registrationDate;
    private boolean active;

    public Movement(String name, String description, BigDecimal amount, LocalDate dueDate,
                    IFormOfPayment formOfPayment, IPayee payee, IMovementCategory category,
                    Set<IAttachment> attachments, EOperationType MovementType,
                    ERepetitionFrequency frequency, UUID groupID) {
        if (name == null || description == null || amount == null || dueDate == null ||
                formOfPayment == null || payee == null || category == null ||
                attachments == null || MovementType == null || frequency == null)
            throw new NullArgumentException();


        if (INCORRECT_NAME_SIZE.test(name.trim()))
            throw new InvalidNameSizeException();
        if (INCORRECT_DESCRIPTION_SIZE.test(description.trim()))
            throw new InvalidDescriptionSizeException();
        if (AMOUNT_IS_ZERO.test(amount))
            throw new AmountEqualZeroException();

        this.ID = UUID.randomUUID();

        if (frequency == NONE) {
            if (groupID == null)
                this.groupID = this.ID;
            else
                this.groupID = groupID;
        } else {
            this.groupID = this.ID;
        }
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.dueDate = dueDate;
        this.formOfPayment = formOfPayment;
        this.payee = payee;
        this.category = category;
        this.attachments = new HashSet<>();
        this.attachments.addAll(attachments);

        this.movementType = MovementType;
        this.frequency = frequency;
        this.accomplished = false;
        this.registrationDate = LocalDate.now();
        this.active = true;
        normalizesAmount();
    }


    public Movement(String name, BigDecimal amount, LocalDate dueDate,
                    IFormOfPayment formOfPayment, IPayee payee, IMovementCategory category,
                    EOperationType MovementType) {
        this(name, EMPTY_DESCRIPTION, amount, dueDate, formOfPayment, payee, category, new HashSet<>(), MovementType, NONE, null);

    }

    public Movement(String name, BigDecimal amount, LocalDate dueDate,
                    IFormOfPayment formOfPayment, IPayee payee, IMovementCategory category,
                    EOperationType MovementType,
                    ERepetitionFrequency frequency, UUID groupID) {
        this(name, EMPTY_DESCRIPTION, amount, dueDate, formOfPayment, payee, category, new HashSet<>(), MovementType, frequency, groupID);
    }

    private Movement(IMovement movement) {
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
        this.groupID = movement.getGroupID();
        this.movementType = movement.isCredit() ? CREDIT : DEBIT;
        this.frequency = movement.getRepetitionFrequency();
        this.accomplished = movement.isAccomplished();
        this.registrationDate = movement.getRegistrationDate();
        this.accomplishDate = movement.getAccomplishDate();
        this.active = movement.isActive();
    }


    /**
     * Returns the unique identifier of the Movement.
     *
     * @return the unique identifier of the Movement.
     */
    @Override
    public UUID getID() {
        return this.ID;
    }

    /**
     * Returns the name of the Operation.
     *
     * @return the name of the Operation.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Returns the monetary value of the movement.
     *
     * @return the monetary value of the movement.
     */
    @Override
    public BigDecimal getAmount() {
        return this.amount;
    }

    /**
     * Returns the due date of the movement.
     *
     * @return the due date of the movement.
     */
    @Override
    public LocalDate getDueDate() {
        return this.dueDate;
    }

    /**
     * Returns the accomplish date.
     * <p>
     * It may be {@code null} if the movement is not yet accomplished.
     *
     * @return the accomplish date.
     */
    @Override
    public LocalDate getAccomplishDate() {
        return this.accomplishDate;
    }

    /**
     * Returns the date of registration of the movement.
     *
     * @return the date of registration of the movement.
     */
    @Override
    public LocalDate getRegistrationDate() {
        return this.registrationDate;
    }

    /**
     * Returns the reference (month / year) of the movement.
     *
     * @return the reference (month / year) of the movement.
     */
    @Override
    public YearMonth getReference() {
        return YearMonth.of(this.dueDate.getYear(), this.dueDate.getMonth());
    }

    /**
     * Returns the description of the movement.
     *
     * @return the description of the movement.
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns the payee of the movement.
     *
     * @return the payee of the movement.
     */
    @Override
    public IPayee getPayee() {
        return this.payee.clone();
    }

    /**
     * Returns the category of the movement.
     *
     * @return the category of the movement.
     */
    @Override
    public IMovementCategory getCategory() {
        return this.category.clone();
    }

    /**
     * Returns the payment method of the transaction.
     *
     * @return the payment method of the transaction.
     */
    @Override
    public IFormOfPayment getFormOfPayment() {
        return this.formOfPayment.clone();
    }

    /**
     * Returns a collection of movement attachments.
     *
     * @return a collection of movement attachments.
     */
    @Override
    public Set<IAttachment> getAttachments() {
        return Collections.unmodifiableSet(this.attachments);
    }

    /**
     * Adds an attachment to the operation.
     *
     * @param newAttachment an attachment.
     * @throws NullArgumentException if the argument is null.
     */
    @Override
    public void addAttachment(IAttachment newAttachment) {
        if (newAttachment == null)
            throw new NullArgumentException();
        this.attachments.add(newAttachment);
    }

    /**
     * Remove an attachment to the operation.
     *
     * @param attachment an existing attachment in the operation.
     * @throws NullArgumentException           if the argument is null.
     * @throws AttachmentDoesNotExistException if the attachment does not exist in the operation.
     */
    @Override
    public void removeAttachment(IAttachment attachment) {
        if (attachment == null)
            throw new NullArgumentException();
        if (this.attachments == null || !this.attachments.contains(attachment))
            throw new AttachmentDoesNotExistException();
        this.attachments.remove(attachment);
    }

    /**
     * Inactivates a movement, it is the equivalent of deleting a movement.
     */
    @Override
    public void inactivate() {
        this.active = false;
    }

    /**
     * Returns if {@code true} if a movement is active, that is, not deleted.
     *
     * @return if {@code true} if a movement is active, that is, not deleted.
     */
    @Override
    public boolean isActive() {
        return this.active;
    }

    /**
     * Returns if {@code true} if a movement is inactive, that is, deleted.
     *
     * @return if {@code true} if a movement is inactive, that is, deleted.
     */
    @Override
    public boolean isInactive() {
        return !this.active;
    }

    /**
     * Returns if the movement is of the debit type.
     * A debit is a operation less that zero.
     *
     * @return {@code true} if the movement is of the debit type.
     */
    @Override
    public boolean isDebit() {
        return (this.amount.compareTo(BigDecimal.ZERO) < 0);
    }

    /**
     * Returns if the movement is of the credit type.
     * A credit is a operation greater that zero.
     *
     * @return {@code true} if the movement is of the credit type.
     */
    @Override
    public boolean isCredit() {
        return (this.amount.compareTo(BigDecimal.ZERO) > 0);
    }

    @Override
    public String toString() {
        return "Operation{" +
                "ID=" + ID +
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


    /**
     * Makes the movement accomplished.
     */
    @Override
    public void accomplish() {
        accomplish(LocalDate.now());
    }

    /**
     * Makes the movement accomplished.
     *
     * @param accomplishDate from occurrence.
     * @throws NullArgumentException if the argument is null.
     */
    @Override
    public void accomplish(LocalDate accomplishDate) {
        if (accomplishDate == null)
            throw new NullArgumentException();
        this.accomplished = true;
        this.accomplishDate = accomplishDate;
    }

    /**
     * Returns whether the movement was accomplished.
     *
     * @return {@code true} whether the movement was accomplished.
     */
    @Override
    public boolean isAccomplished() {
        return this.accomplished;
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
    public void updateCategory(IMovementCategory newCategory) {
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
        this.movementType = type;
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
    @Transient
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
    @Transient
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
    @Transient
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
    @Transient
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
        Movement movement = (Movement) o;
        return accomplished == movement.accomplished && ID.equals(movement.ID) && name.equals(movement.name) &&
                description.equals(movement.description) && amount.equals(movement.amount) &&
                dueDate.equals(movement.dueDate) && formOfPayment.equals(movement.formOfPayment) &&
                payee.equals(movement.payee) && category.equals(movement.category) &&
                attachments.equals(movement.attachments) && movementType == movement.movementType &&
                frequency == movement.frequency && groupID.equals(movement.groupID) &&
                Objects.equals(accomplishDate, movement.accomplishDate) &&
                registrationDate.equals(movement.registrationDate) && active == movement.isActive();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movement movement = (Movement) o;
        return ID.equals(movement.ID);
    }

    @Override
    public int hashCode() {
        return 31 * Objects.hash(ID);
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

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure
     * {@code sgn(x.compareTo(y)) == -sgn(y.compareTo(x))}
     * for all {@code x} and {@code y}.  (This
     * implies that {@code x.compareTo(y)} must throw an exception iff
     * {@code y.compareTo(x)} throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
     * {@code x.compareTo(z) > 0}.
     *
     * <p>Finally, the implementor must ensure that {@code x.compareTo(y)==0}
     * implies that {@code sgn(x.compareTo(z)) == sgn(y.compareTo(z))}, for
     * all {@code z}.
     *
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
     * class that implements the {@code Comparable} interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     *
     * <p>In the foregoing description, the notation
     * {@code sgn(}<i>expression</i>{@code )} designates the mathematical
     * <i>signum</i> function, which is defined to return one of {@code -1},
     * {@code 0}, or {@code 1} according to whether the value of
     * <i>expression</i> is negative, zero, or positive, respectively.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(IMovement o) {
        return this.getDueDate().compareTo(o.getDueDate());
    }

    protected Movement() {
    }

    @SuppressWarnings("unused")
    private void setGroupID(UUID groupID) {
        this.groupID = groupID;
    }


    @SuppressWarnings("unused")
    private void setMovementType(EOperationType movementType) {
        this.movementType = movementType;
    }

    @SuppressWarnings("unused")
    private void setFrequency(ERepetitionFrequency frequency) {
        this.frequency = frequency;
    }

    @SuppressWarnings("unused")
    private EOperationType getMovementType() {
        return movementType;
    }

    @SuppressWarnings("unused")
    private ERepetitionFrequency getFrequency() {
        return frequency;
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
    private void setCategory(IMovementCategory category) {
        this.category = category;
    }

    @SuppressWarnings("unused")
    private void setAttachments(Set<IAttachment> attachments) {
        this.attachments = attachments;
    }

    @SuppressWarnings("unused")
    private void setID(UUID ID) {
        this.ID = ID;
    }

    private void normalizesAmount() {
        if (this.movementType == CREDIT && this.amount.compareTo(BigDecimal.ZERO) < 0) {
            this.amount = this.amount.multiply(new BigDecimal("-1"));
        } else if (this.movementType == DEBIT && this.amount.compareTo(BigDecimal.ZERO) > 0) {
            this.amount = this.amount.multiply(new BigDecimal("-1"));
        }
    }
}
