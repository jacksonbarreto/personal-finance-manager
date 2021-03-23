package bll.entities;

import java.util.Objects;
import java.util.UUID;

import static bll.entities.Utilities.isInvalidName;
import static bll.entities.Utilities.isNullArgument;

import bll.exceptions.InvalidNameSizeException;
import bll.exceptions.NullArgumentException;

public class FormOfPayment implements IFormOfPayment {
    private UUID id;
    private String name;
    private boolean active;

    public FormOfPayment(String name, boolean active) {
        isInvalidName(name, MINIMUM_NAME_SIZE, MAXIMUM_NAME_SIZE);
        this.name = name;
        this.active = active;
        this.id = UUID.randomUUID();
    }

    public FormOfPayment(String name) {
        isInvalidName(name, MINIMUM_NAME_SIZE, MAXIMUM_NAME_SIZE);
        this.name = name;
        this.active = true;
        this.id = UUID.randomUUID();
    }

    public FormOfPayment(IFormOfPayment formOfPayment) {
        isNullArgument(formOfPayment);
        this.name = formOfPayment.getName();
        this.active = formOfPayment.isActive();
        this.id = formOfPayment.getID();
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
        isInvalidName(newName, MINIMUM_NAME_SIZE, MAXIMUM_NAME_SIZE);
        this.name = newName;
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
        return Objects.hash(id, name, active);
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

    private FormOfPayment() {
    }

    private UUID getId() {
        return id;
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
