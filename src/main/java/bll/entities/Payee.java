package bll.entities;

import bll.exceptions.DifferentObjectException;
import bll.exceptions.InvalidNameSizeException;
import bll.exceptions.NullArgumentException;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Payee implements IPayee {
    @Id
    private UUID id;
    private String name;
    private boolean active;

    public Payee(String name, boolean active) {
        if (name == null)
            throw new NullArgumentException();
        if (INCORRECT_NAME_SIZE.test(name.trim()))
            throw new InvalidNameSizeException();
        this.name = name.trim();
        this.active = active;
        this.id = UUID.randomUUID();
    }

    public Payee(String name) {
        this(name, true);
    }

    public Payee(IPayee payee) {
        if (payee == null)
            throw new NullArgumentException();
        this.id = payee.getID();
        this.name = payee.getName();
        this.active = payee.isActive();
    }

    /**
     * Updates all its attributes from an external copy.
     *
     * @param externalCopy of the original element.
     * @throws DifferentObjectException if the object sent does not have the same id.
     * @throws NullArgumentException    if the argument is null.
     */
    @Override
    public void autoUpdate(IPayee externalCopy) {
        if (externalCopy == null)
            throw new NullArgumentException();
        if (!this.id.equals(externalCopy.getID()))
            throw new DifferentObjectException();

        this.name = externalCopy.getName();
        this.active = externalCopy.isActive();
    }

    /**
     * Returns the name of the payee.
     *
     * @return the name of the payee.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Allows you to change the name of the payee.
     *
     * @param newName the new name of the payee.
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
     * Returns the unique identifier of the payee.
     *
     * @return the unique identifier of the payee.
     */
    @Override
    public UUID getID() {
        return this.id;
    }

    /**
     * Indicates whether some other IEmail is "equal to" this one.
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
        Payee payee = (Payee) obj;
        return active == payee.active && id.equals(payee.id) && name.equals(payee.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payee payee = (Payee) o;
        return id.equals(payee.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Indicates whether the payee is active.
     *
     * @return {@code true} if payee is active.
     */
    @Override
    public boolean isActive() {
        return active;
    }

    /**
     * Indicates whether the payee is inactive.
     *
     * @return {@code true} if payee is inactive.
     */
    @Override
    public boolean isInactive() {
        return !active;
    }

    /**
     * Inactivates the payee.
     */
    @Override
    public void inactivate() {
        this.active = false;
    }

    /**
     * Activates the payee.
     */
    @Override
    public void activate() {
        this.active = true;
    }

    public IPayee clone() {
        return new Payee(this);
    }

    @Override
    public String toString() {
        return "Payee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", active=" + active +
                '}';
    }


    protected Payee() {

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
