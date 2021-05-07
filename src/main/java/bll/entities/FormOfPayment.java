package bll.entities;

import bll.exceptions.DifferentObjectException;
import bll.exceptions.InvalidNameSizeException;
import bll.exceptions.NullArgumentException;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;
import java.util.UUID;

@Entity
public class FormOfPayment implements IFormOfPayment {
    @Id
    private UUID id;
    private String name;
    private boolean active;

    public FormOfPayment(String name, boolean active) {
        if (name == null)
            throw new NullArgumentException();
        if (INCORRECT_NAME_SIZE.test(name.trim()))
            throw new InvalidNameSizeException();
        this.name = name.trim();
        this.active = active;
        this.id = UUID.randomUUID();
    }

    public FormOfPayment(String name) {
        this(name, true);
    }

    public FormOfPayment(IFormOfPayment formOfPayment) {
        if (formOfPayment == null)
            throw new NullArgumentException();
        this.name = formOfPayment.getName();
        this.active = formOfPayment.isActive();
        this.id = formOfPayment.getID();
    }

    @Override
    public void autoUpdate(IFormOfPayment externalCopy) {
        if (externalCopy == null)
            throw new NullArgumentException();
        if (!this.id.equals(externalCopy.getID()))
            throw new DifferentObjectException();

        this.name = externalCopy.getName();
        this.active = externalCopy.isActive();
    }

    /**
     * Returns the unique identifier of the FormOfPayment.
     *
     * @return the unique identifier of the FormOfPayment.
     */
    @Override
    public UUID getID() {
        return this.id;
    }

    /**
     * Returns the name of the FormOfPayment.
     *
     * @return the name of the FormOfPayment.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Allows you to change the name of the FormOfPayment.
     *
     * @param newName the new name of the FormOfPayment.
     * @throws InvalidNameSizeException if the size of the name attribute
     *                                  does not correspond to the minimum and / or maximum limits
     *                                  defined in an MINIMUM_NAME_SIZE and MAXIMUM_NAME_SIZE.
     * @throws NullArgumentException    if the argument is null.
     */
    @Override
    public void updateName(String newName) {
        if (newName == null)
            throw new NullArgumentException();
        if (INCORRECT_NAME_SIZE.test(newName.trim()))
            throw new InvalidNameSizeException();
        this.name = newName.trim();
    }

    /**
     * Indicates whether some other FormOfPayment is "equal to" this one.
     * <p>
     * It is only the same when both objects have all their attributes equal.
     *
     * @param obj an instance of Object.
     * @return {@code true} if only the same when both objects have all their attributes equal.
     */
    @Override
    public boolean isDeepEquals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FormOfPayment that = (FormOfPayment) obj;
        return active == that.active && id.equals(that.id) && name.equals(that.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FormOfPayment that = (FormOfPayment) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Indicates whether the FormOfPayment is active.
     *
     * @return {@code true} if FormOfPayment is active.
     */
    @Override
    public boolean isActive() {
        return this.active;
    }

    /**
     * Indicates whether the FormOfPayment is inactive.
     *
     * @return {@code true} if FormOfPayment is inactive.
     */
    @Override
    public boolean isInactive() {
        return !this.active;
    }

    /**
     * Inactivates the FormOfPayment.
     */
    @Override
    public void inactivate() {
        this.active = false;
    }

    /**
     * Activates the FormOfPayment.
     */
    @Override
    public void activate() {
        this.active = true;
    }

    /**
     * Returns a clone of the current object.
     *
     * @return a clone of the current instance.
     */
    @Override
    public IFormOfPayment clone() {
        return new FormOfPayment(this);
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
    public int compareTo(IFormOfPayment o) {
        return this.getName().toLowerCase().compareTo(o.getName().toLowerCase());
    }

    protected FormOfPayment() {
    }

    @SuppressWarnings("unused")
    private UUID getId() {
        return id;
    }

    @SuppressWarnings("unused")
    private void setId(UUID id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    private void setName(String name) {
        this.name = name;
    }

    @SuppressWarnings("unused")
    private void setActive(boolean active) {
        this.active = active;
    }
}
