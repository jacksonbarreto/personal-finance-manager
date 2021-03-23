package bll.entities;

import bll.exceptions.InvalidNameSizeException;
import bll.exceptions.NullArgumentException;

import java.util.Objects;
import java.util.UUID;

public class Payee implements IPayee {
    private UUID id;
    private String name;
    private boolean active;

    public Payee(String name, boolean active) {
        isInvalidName(name);
        this.name = name;
        this.active = active;
        this.id = UUID.randomUUID();
    }

    public Payee(String name) {
        this(name, true);
    }

    public Payee(IPayee payee) {
        isNullArgument(payee);
        this.id = payee.getID();
        this.name = payee.getName();
        this.active = payee.isActive();
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
        isInvalidName(newName);
        this.name = newName;
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
        return Objects.hash(id, name, active);
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

    private void isInvalidName(String name) {
        isNullArgument(name);
        if (INVALID_NAME_SIZE.test(name))
            throw new InvalidNameSizeException();
    }

    private void isNullArgument(Object obj) {
        if (obj == null)
            throw new NullArgumentException();
    }


    private void setId(UUID id) {
        this.id = id;
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setActive(boolean active) {
        this.active = active;
    }
}
