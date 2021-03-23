package bll.entities;

import java.util.Objects;
import java.util.UUID;

import bll.exceptions.InvalidNameSizeException;
import bll.exceptions.NullArgumentException;

import static bll.entities.Utilities.*;
import static bll.entities.Utilities.isNullArgument;

public class TransactionCategory implements ITransactionCategory {

    private String name;
    private String imgURI;
    private UUID id;
    private boolean active;

    public TransactionCategory(String name, String imgURI, boolean active) {
        isNullArgument(imgURI);
        isInvalidName(name, MINIMUM_NAME_SIZE, MAXIMUM_NAME_SIZE);
        this.name = name;
        this.imgURI = imgURI;
        this.active = active;
        this.id = UUID.randomUUID();
    }

    public TransactionCategory(String name, String imgURI) {
        isNullArgument(imgURI);
        isInvalidName(name, MINIMUM_NAME_SIZE, MAXIMUM_NAME_SIZE);
        this.name = name;
        this.imgURI = imgURI;
        this.active = true;
        this.id = UUID.randomUUID();
    }

    public TransactionCategory(String name) {
        isInvalidName(name, MINIMUM_NAME_SIZE, MAXIMUM_NAME_SIZE);
        this.name = name;
        this.active = true;
        this.id = UUID.randomUUID();
    }

    public TransactionCategory(ITransactionCategory transactionCategory) {
        isNullArgument(transactionCategory);
        this.name = transactionCategory.getName();
        this.active = transactionCategory.isActive();
        this.id = transactionCategory.getID();
        this.imgURI = transactionCategory.getImgURI();
    }

    /**
     * Returns the unique identifier of the Category.
     *
     * @return the unique identifier of the Category.
     */
    @Override
    public UUID getID() {
        return this.id;
    }

    /**
     * Returns the name of the Category.
     *
     * @return the name of the Category.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Allows you to change the name of the Category.
     *
     * @param newName the new name of the Category.
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
     * Returns the URI of the image that represents the category.
     *
     * @return the URI of the image that represents the category.
     */
    @Override
    public String getImgURI() {
        return this.imgURI;
    }

    /**
     * Changes the URI for accessing the Category image.
     *
     * @param newURI new URI for accessing the Category image.
     */
    @Override
    public void updateImgURI(String newURI) {
        isNullArgument(newURI);
        this.imgURI = newURI;
    }

    /**
     * Indicates whether some other ICategory is "equal to" this one.
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
        TransactionCategory that = (TransactionCategory) obj;
        return active == that.active && name.equals(that.name) && Objects.equals(imgURI, that.imgURI) && id.equals(that.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionCategory that = (TransactionCategory) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, imgURI, id, active);
    }

    /**
     * Indicates whether the Category is active.
     *
     * @return {@code true} if Category is active.
     */
    @Override
    public boolean isActive() {
        return this.active;
    }

    /**
     * Indicates whether the Category is inactive.
     *
     * @return {@code true} if Category is inactive.
     */
    @Override
    public boolean isInactive() {
        return !this.active;
    }

    /**
     * Inactivates the Category.
     */
    @Override
    public void inactivate() {
        this.active = false;
    }

    /**
     * Activates the Category.
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
    public ITransactionCategory clone() {
        return new TransactionCategory(this);
    }

    private TransactionCategory() {

    }

    private void setName(String name) {
        this.name = name;
    }

    private void setImgURI(String imgURI) {
        this.imgURI = imgURI;
    }

    private UUID getId() {
        return id;
    }

    private void setId(UUID id) {
        this.id = id;
    }

    private void setActive(boolean active) {
        this.active = active;
    }
}
