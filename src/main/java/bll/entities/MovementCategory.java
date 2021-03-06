package bll.entities;

import bll.exceptions.DifferentObjectException;
import bll.exceptions.InvalidNameSizeException;
import bll.exceptions.NullArgumentException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.net.URI;
import java.util.Objects;
import java.util.UUID;

@Entity
public class MovementCategory implements IMovementCategory {
    @Id
    private UUID id;
    @Column(nullable = false)
    private String name;
    private URI image;
    private boolean active;
    private boolean publicCategory;

    public MovementCategory(String name, URI image, boolean active, boolean publicCategory) {
        if (name == null)
            throw new NullArgumentException();
        if (INCORRECT_NAME_SIZE.test(name.trim()))
            throw new InvalidNameSizeException();
        this.name = name.trim();
        this.image = image;
        this.active = active;
        this.id = UUID.randomUUID();
        this.publicCategory = publicCategory;
    }

    public MovementCategory(String name, URI image) {
        this(name, image, true, true);
    }

    public MovementCategory(String name) {
        this(name, null, true, false);
    }

    public static IMovementCategory createPublicCategory(String name, URI image) {
        return new MovementCategory(name, image, true, true);
    }

    public MovementCategory(IMovementCategory transactionCategory) {
        if (transactionCategory == null)
            throw new NullArgumentException();
        this.name = transactionCategory.getName();
        this.active = transactionCategory.isActive();
        this.id = transactionCategory.getID();
        this.image = transactionCategory.getImage();
        this.publicCategory = transactionCategory.isPublic();
    }

    /**
     * Updates all its attributes from an external copy.
     *
     * @param externalCopy of the original element.
     * @throws DifferentObjectException if the object sent does not have the same id.
     * @throws NullArgumentException    if the argument is null.
     */
    @Override
    public void autoUpdate(IMovementCategory externalCopy) {
        if (externalCopy == null)
            throw new NullArgumentException();
        if (!this.id.equals(externalCopy.getID()))
            throw new DifferentObjectException();

        this.name = externalCopy.getName();
        this.active = externalCopy.isActive();
        this.image = externalCopy.getImage();
        this.publicCategory = externalCopy.isPublic();
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
    public URI getImage() {
        return this.image;
    }

    /**
     * Changes the URI for accessing the Category image.
     *
     * @param newImage to update.
     */
    @Override
    public void updateImage(URI newImage) {
        this.image = newImage;
    }

    /**
     * Returns true if the category is public.
     *
     * @return {@code true} if the category is public.
     */
    @Override
    public boolean isPublic() {
        return this.publicCategory;
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
        MovementCategory that = (MovementCategory) obj;
        return active == that.active && name.equals(that.name) && Objects.equals(image, that.image) && id.equals(that.id) && publicCategory == that.publicCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovementCategory that = (MovementCategory) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
    public IMovementCategory clone() {
        return new MovementCategory(this);
    }

    @Override
    public String toString() {
        return "TransactionCategory{" +
                "name='" + name + '\'' +
                ", imgURI='" + image + '\'' +
                ", id=" + id +
                ", active=" + active +
                '}';
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
    public int compareTo(IMovementCategory o) {
        return this.getName().toLowerCase().compareTo(o.getName().toLowerCase());
    }

    protected MovementCategory() {

    }

    @SuppressWarnings("unused")
    private void setName(String name) {
        this.name = name;
    }

    @SuppressWarnings("unused")
    private void setImage(URI imgURI) {
        this.image = imgURI;
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
