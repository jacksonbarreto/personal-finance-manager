package bll.entities;

import bll.exceptions.*;
import bll.valueObjects.IAttachment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

public abstract class Operation implements IOperation {
    protected UUID ID;
    protected String name;
    protected String description;
    protected BigDecimal amount;
    protected LocalDate dueDate;
    protected IFormOfPayment formOfPayment;
    protected IPayee payee;
    protected ITransactionCategory category;
    protected Set<IAttachment> attachments;

    protected Operation(String name, String description, BigDecimal amount, LocalDate dueDate,
                        IFormOfPayment formOfPayment, IPayee payee, ITransactionCategory category,
                        Set<IAttachment> attachments) {
        if (name == null || description == null || amount == null || dueDate == null ||
                formOfPayment == null || payee == null || category == null || attachments == null)
            throw new NullArgumentException();
        if (INCORRECT_NAME_SIZE.test(name.trim()))
            throw new InvalidNameSizeException();
        if (INCORRECT_DESCRIPTION_SIZE.test(description.trim()))
            throw new InvalidDescriptionSizeException();
        if (AMOUNT_IS_ZERO.test(amount))
            throw new AmountEqualZeroException();

        this.name = name;
        this.description = description;
        this.amount = amount;
        this.dueDate = dueDate;
        this.formOfPayment = formOfPayment;
        this.payee = payee;
        this.category = category;
        this.attachments = attachments;
        this.ID = UUID.randomUUID();
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
    public ITransactionCategory getCategory() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return ID.equals(operation.ID);
    }


    @Override
    public int hashCode() {
        return Objects.hash(ID, name, description, amount, dueDate, formOfPayment, payee, category, attachments);
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
        Operation operation = (Operation) o;
        return ID.equals(operation.ID) && name.equals(operation.name) && description.equals(operation.description) && amount.equals(operation.amount) && dueDate.equals(operation.dueDate) && formOfPayment.equals(operation.formOfPayment) && payee.equals(operation.payee) && category.equals(operation.category) && attachments.equals(operation.attachments);

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

    @SuppressWarnings("unused")
    protected Operation() {
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
    private void setID(UUID ID) {
        this.ID = ID;
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
    public int compareTo(IOperation o) {
        return this.getDueDate().compareTo(o.getDueDate());
    }
}
