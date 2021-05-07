package bll.entities;

import bll.exceptions.DifferentObjectException;
import bll.exceptions.InvalidNameSizeException;
import bll.exceptions.NullArgumentException;

import java.io.Serializable;
import java.net.URI;
import java.util.UUID;
import java.util.function.Predicate;

public interface IMovementCategory extends Serializable, Comparable<IMovementCategory> {

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
    void autoUpdate(IMovementCategory externalCopy);

    /**
     * Returns the unique identifier of the Category.
     *
     * @return the unique identifier of the Category.
     */
    UUID getID();

    /**
     * Returns the name of the Category.
     *
     * @return the name of the Category.
     */
    String getName();

    /**
     * Allows you to change the name of the Category.
     *
     * @param newName the new name of the Category.
     * @throws InvalidNameSizeException if the size of the name attribute
     *                                  does not correspond to the minimum and / or maximum limits
     *                                  defined in an MINIMUM_NAME_SIZE and MAXIMUM_NAME_SIZE.
     * @throws NullArgumentException    if the argument is null.
     */
    void updateName(String newName);

    /**
     * Returns the URI of the image that represents the category.
     *
     * @return the URI of the image that represents the category.
     */
    URI getImage();

    /**
     * Changes the URI for accessing the Category image.
     *
     * @param newImage
     */
    void updateImage(URI newImage);

    /**
     * Returns true if the category is public.
     *
     * @return {@code true} if the category is public.
     */
    boolean isPublic();

    /**
     * Returns a string representation of the Category object.
     *
     * @return a string representation of the Category object.
     */
    String toString();


    /**
     * Indicates whether some other ICategory is "equal to" this one.
     * <p>
     * It is only the same when both objects have ID equal.
     *
     * @param obj an instance of Object.
     * @return {@code true} only if both objects have the same ID.
     */
    boolean equals(Object obj);

    /**
     * Indicates whether some other ICategory is "equal to" this one.
     * <p>
     * It is only the same when both objects have all their attributes equal.
     *
     * @param obj an instance of Object.
     * @return {@code true} if only the same when both objects have all their attributes equal.
     */
    boolean isDeepEquals(Object obj);

    /**
     * Indicates whether the Category is active.
     *
     * @return {@code true} if Category is active.
     */
    boolean isActive();

    /**
     * Indicates whether the Category is inactive.
     *
     * @return {@code true} if Category is inactive.
     */
    boolean isInactive();

    /**
     * Inactivates the Category.
     */
    void inactivate();

    /**
     * Activates the Category.
     */
    void activate();

    /**
     * Returns a clone of the current object.
     *
     * @return a clone of the current instance.
     */
    IMovementCategory clone();

}
