package bll.entities;

import bll.exceptions.InvalidNameSizeException;
import bll.exceptions.NullArgumentException;

import java.util.Objects;
import java.util.UUID;

public class TransactionCategory implements ITransactionCategory {

    private String name;
    private String imgURI;
    private UUID id;
    private boolean active;

    public TransactionCategory(String name, String imgURI, boolean active) {
        if (name == null || imgURI == null)
            throw new NullArgumentException();
        if (INCORRECT_NAME_SIZE.test(name.trim()))
            throw new InvalidNameSizeException();
        this.name = name.trim();
        this.imgURI = imgURI.trim();
        this.active = active;
        this.id = UUID.randomUUID();
    }

    public TransactionCategory(String name, String imgURI) {
        this(name, imgURI, true);
    }

    public TransactionCategory(String name) {
        this(name, "", true);
    }

    public TransactionCategory(ITransactionCategory transactionCategory) {
        if (transactionCategory == null)
            throw new NullArgumentException();
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
        if (newName == null)
            throw new NullArgumentException();
        if (INCORRECT_NAME_SIZE.test(newName.trim()))
            throw new InvalidNameSizeException();
        this.name = newName.trim();
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
        if (newURI == null)
            throw new NullArgumentException();
        this.imgURI = newURI.trim();
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

    @Override
    public String toString() {
        return "TransactionCategory{" +
                "name='" + name + '\'' +
                ", imgURI='" + imgURI + '\'' +
                ", id=" + id +
                ", active=" + active +
                '}';
    }

    @SuppressWarnings("unused")
    private TransactionCategory() {

    }
    @SuppressWarnings("unused")
    private void setName(String name) {
        this.name = name;
    }
    @SuppressWarnings("unused")
    private void setImgURI(String imgURI) {
        this.imgURI = imgURI;
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
    private void setActive(boolean active) {
        this.active = active;
    }
}
