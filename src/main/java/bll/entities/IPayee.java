package bll.entities;

import java.io.Serializable;
import java.util.UUID;
import java.util.function.Predicate;

import bll.exceptions.InvalidNameSizeException;
import bll.exceptions.NullArgumentException;

public interface IPayee extends Serializable {

    int MINIMUM_NAME_SIZE = 3;
    int MAXIMUM_NAME_SIZE = 30;
    Predicate<String> INVALID_NAME_SIZE = s -> s.length() < MINIMUM_NAME_SIZE || s.length() > MAXIMUM_NAME_SIZE;

    /**
     * Returns the name of the payee.
     *
     * @return the name of the payee.
     */
    String getName();

    /**
     * Allows you to change the name of the payee.
     *
     * @param newName the new name of the payee.
     * @throws InvalidNameSizeException if the size of the name attribute
     *                                  does not correspond to the minimum and / or maximum limits
     *                                  defined in an MINIMUM_NAME_SIZE and MAXIMUM_NAME_SIZE.
     * @throws NullArgumentException    if the argument is null.
     */
    void updateName(String newName);


    /**
     * Returns the unique identifier of the payee.
     *
     * @return the unique identifier of the payee.
     */
    UUID getID();

    /**
     * Returns a string representation of the payee object.
     *
     * @return a string representation of the payee object.
     */
    String toString();


    /**
     * Indicates whether some other IPayee is "equal to" this one.
     * <p>
     * It is only the same when both objects have ID equal.
     *
     * @param obj an instance of Object.
     * @return {@code true} only if both objects have the same ID.
     */
    boolean equals(Object obj);

    /**
     * Indicates whether some other IPayee is "equal to" this one.
     * <p>
     * It is only the same when both objects have all their attributes equal.
     *
     * @param obj an instance of Object.
     * @return {@code true} if only the same when both objects have all their attributes equal.
     */
    boolean isDeepEquals(Object obj);

    /**
     * Indicates whether the payee is active.
     *
     * @return {@code true} if payee is active.
     */
    boolean isActive();

    /**
     * Indicates whether the payee is inactive.
     *
     * @return {@code true} if payee is inactive.
     */
    boolean isInactive();

    /**
     * Inactivates the payee.
     */
    void inactivate();

    /**
     * Activates the payee.
     */
    void activate();

    /**
     * Returns a clone of the current object.
     *
     * @return a clone of the current instance.
     */
    IPayee clone();
}
