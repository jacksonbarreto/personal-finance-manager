package bll.entities;

import bll.exceptions.DifferentObjectException;
import bll.exceptions.InvalidNameSizeException;
import bll.exceptions.NullArgumentException;

import java.io.Serializable;
import java.util.UUID;
import java.util.function.Predicate;

public interface IFormOfPayment extends Serializable {
    int MINIMUM_NAME_SIZE = 3;
    int MAXIMUM_NAME_SIZE = 30;
    Predicate<String> INCORRECT_NAME_SIZE = (s) -> (s.length() < MINIMUM_NAME_SIZE || s.length() > MAXIMUM_NAME_SIZE);

    /**
     * Updates all its attributes from an external copy.
     *
     * @param externalCopy of the original element.
     * @throws DifferentObjectException if the object sent does not have the same id.
     * @throws NullArgumentException    if the argument is null.
     */
    void autoUpdate(IFormOfPayment externalCopy);

    /**
     * Returns the unique identifier of the FormOfPayment.
     *
     * @return the unique identifier of the FormOfPayment.
     */
    UUID getID();

    /**
     * Returns the name of the FormOfPayment.
     *
     * @return the name of the FormOfPayment.
     */
    String getName();

    /**
     * Allows you to change the name of the FormOfPayment.
     *
     * @param newName the new name of the FormOfPayment.
     * @throws InvalidNameSizeException if the size of the name attribute
     *                                  does not correspond to the minimum and / or maximum limits
     *                                  defined in an MINIMUM_NAME_SIZE and MAXIMUM_NAME_SIZE.
     * @throws NullArgumentException    if the argument is null.
     */
    void updateName(String newName);


    /**
     * Returns a string representation of the FormOfPayment object.
     *
     * @return a string representation of the FormOfPayment object.
     */
    String toString();


    /**
     * Indicates whether some other FormOfPayment is "equal to" this one.
     * <p>
     * It is only the same when both objects have ID equal.
     *
     * @param obj an instance of Object.
     * @return {@code true} only if both objects have the same ID.
     */
    boolean equals(Object obj);

    /**
     * Indicates whether some other FormOfPayment is "equal to" this one.
     * <p>
     * It is only the same when both objects have all their attributes equal.
     *
     * @param obj an instance of Object.
     * @return {@code true} if only the same when both objects have all their attributes equal.
     */
    boolean isDeepEquals(Object obj);

    /**
     * Indicates whether the FormOfPayment is active.
     *
     * @return {@code true} if FormOfPayment is active.
     */
    boolean isActive();

    /**
     * Indicates whether the FormOfPayment is inactive.
     *
     * @return {@code true} if FormOfPayment is inactive.
     */
    boolean isInactive();

    /**
     * Inactivates the FormOfPayment.
     */
    void inactivate();

    /**
     * Activates the FormOfPayment.
     */
    void activate();

    /**
     * Returns a clone of the current object.
     *
     * @return a clone of the current instance.
     */
    IFormOfPayment clone();
}
